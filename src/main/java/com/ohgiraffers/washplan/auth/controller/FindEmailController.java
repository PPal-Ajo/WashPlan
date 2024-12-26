package com.ohgiraffers.washplan.auth.controller;

import com.ohgiraffers.washplan.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/email/find-id")
public class FindEmailController {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final Map<String, CodeData> findIdCodeStorage = new HashMap<>();

    @Autowired
    public FindEmailController(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendFindIdEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // 이메일 존재 여부 확인
        boolean exists = userService.checkEmailExistence(email);
        if (!exists) {
            return ResponseEntity.ok(Collections.singletonMap("exists", false)); // 존재하지 않음
        }

        // 인증번호 생성 및 전송
        String code = generateCode();
        long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000;
        findIdCodeStorage.put(email, new CodeData(code, expiryTime));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("아이디 찾기 인증 코드");
        message.setText("인증 코드: " + code + " (유효시간: 5분)");

        mailSender.send(message);

        return ResponseEntity.ok(Collections.singletonMap("exists", true)); // 존재함
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyFindIdCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        CodeData codeData = findIdCodeStorage.get(email);
        if (codeData != null && code.equals(codeData.getCode())
                && System.currentTimeMillis() <= codeData.getExpiryTime()) {
            findIdCodeStorage.remove(email);
            String userId = userService.getUserIdByEmail(email); // 사용자 아이디 가져오기
            return ResponseEntity.ok(Collections.singletonMap("userId", userId));
        }
        return ResponseEntity.ok(Collections.singletonMap("success", false));
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(999999 - 100000) + 100000);
    }
}
