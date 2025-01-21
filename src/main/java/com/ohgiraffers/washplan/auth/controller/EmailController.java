package com.ohgiraffers.washplan.auth.controller;

import com.ohgiraffers.washplan.user.model.dao.UserMapper;
import com.ohgiraffers.washplan.user.model.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final Map<String, CodeData> codeStorage = new HashMap<>();
    private final UserMapper userMapper;

    @Autowired
    public EmailController(JavaMailSender mailSender, UserService userService, UserMapper userMapper) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * 이메일로 인증 코드 전송
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = generateCode();
        long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5분 후 만료

        codeStorage.put(email, new CodeData(code, expiryTime));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("WashPlan 이메일 인증 코드");
        message.setText("인증 코드: " + code + " (유효시간: 5분)");

        mailSender.send(message);

        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    /**
     * 인증 코드 확인
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        String code = request.get("code");

        CodeData codeData = codeStorage.get(email);
        if (codeData != null && code.equals(codeData.getCode()) && System.currentTimeMillis() <= codeData.getExpiryTime()) {
            // 성공 시 코드 삭제
            codeStorage.remove(email);

            // 세션에 이메일 저장
            session.setAttribute("verifiedEmail", email);
            System.out.println("세션에 저장된 이메일: " + session.getAttribute("verifiedEmail"));

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.ok(Collections.singletonMap("success", false));
        }
    }

    /**
     * 중복 이메일 확인
     */
    @PostMapping("/check-duplicate")
    public ResponseEntity<?> checkEmailDuplicate(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isDuplicate = userService.isEmailDuplicate(email);

        return ResponseEntity.ok(Collections.singletonMap("isDuplicate", isDuplicate));
    }

    /**
     * 인증 코드 생성
     */
    private String generateCode() {
        return String.valueOf(new Random().nextInt(999999 - 100000) + 100000);
    }


    @GetMapping("/get-session-email")
    public ResponseEntity<?> getSessionEmail(HttpSession session) {
        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        if (verifiedEmail != null) {
            return ResponseEntity.ok(Collections.singletonMap("verifiedEmail", verifiedEmail));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "No verified email in session"));
        }
    }

    @PostMapping("/check-existence")
    public ResponseEntity<Map<String, Boolean>> checkEmailExistence(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean exists = userService.checkEmailExistence(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find-pwd/send")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> sendPasswordResetCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String userId = request.get("userId");
        Map<String, Boolean> response = new HashMap<>();

        try {
            // 이메일 형식 검사
            if (!isValidEmail(email)) {
                response.put("success", false);
                return ResponseEntity.ok(response);
            }

            // 이메일과 아이디가 일치하는지 확인
            if (!userMapper.existsByEmailAndUserId(email, userId)) {
                response.put("success", false);
                return ResponseEntity.ok(response);
            }

            // 기존 코드 저장소 사용
            String code = generateCode();
            long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5분 후 만료
            codeStorage.put(email, new CodeData(code, expiryTime));

            // 이메일 발송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("WashPlan 비밀번호 재설정 인증번호");
            message.setText("인증번호: " + code + "\n(유효시간: 5분)");
            mailSender.send(message);

            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 로그 확인을 위해 추가
            response.put("success", false);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/find-pwd/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> verifyPasswordResetCode(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        String code = request.get("code");
        String userId = request.get("userId");
        Map<String, Boolean> response = new HashMap<>();

        CodeData codeData = codeStorage.get(email);
        if (codeData != null && code.equals(codeData.getCode()) && System.currentTimeMillis() <= codeData.getExpiryTime()) {
            // 성공 시 코드 삭제
            codeStorage.remove(email);
            session.setAttribute("resetEmail", email);
            session.setAttribute("resetUserId", userId);
            response.put("success", true);
        } else {
            response.put("success", false);
        }

        return ResponseEntity.ok(response);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}