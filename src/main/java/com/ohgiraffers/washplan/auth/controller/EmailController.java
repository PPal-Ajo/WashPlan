package com.ohgiraffers.washplan.auth.controller;

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
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, CodeData> codeStorage = new HashMap<>();

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = generateCode();
        long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5분 후 만료

        codeStorage.put(email, new CodeData(code, expiryTime));

        System.out.println("Generated code for " + email + ": " + code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("WashPlan 이메일 인증 코드");
        message.setText("인증 코드: " + code + " (유효시간: 5분)");

        mailSender.send(message);

        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        CodeData codeData = codeStorage.get(email);
        if (codeData != null && code.equals(codeData.getCode()) && System.currentTimeMillis() <= codeData.getExpiryTime()) {
            codeStorage.remove(email); // 성공 시 코드 삭제
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.ok(Collections.singletonMap("success", false));
        }
    }

    private static class CodeData {
        private final String code;
        private final long expiryTime;

        public CodeData(String code, long expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }

        public String getCode() {
            return code;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(999999 - 100000) + 100000);
    }
}