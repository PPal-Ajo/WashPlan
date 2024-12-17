package com.ohgiraffers.washplan.auth.controller;

import com.ohgiraffers.washplan.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final Map<String, CodeData> codeStorage = new HashMap<>();

    @Autowired
    public EmailController(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
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
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        CodeData codeData = codeStorage.get(email);
        if (codeData != null && code.equals(codeData.getCode()) && System.currentTimeMillis() <= codeData.getExpiryTime()) {
            codeStorage.remove(email); // 인증 성공 시 코드 삭제
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
}