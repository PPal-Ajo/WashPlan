package com.ohgiraffers.washplan.auth.controller;

import com.ohgiraffers.washplan.user.model.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/check-credentials")
    public ResponseEntity<Map<String, Boolean>> checkCredentials(@RequestBody Map<String, String> credentials, HttpSession session) {
        String email = credentials.get("email");
        String userId = credentials.get("userId");

        boolean isValid = userService.validateCredentials(email, userId);
        if (isValid) {
            // 세션에 이메일과 아이디 저장
            session.setAttribute("resetEmail", email);
            session.setAttribute("resetUserId", userId);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isValid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request, HttpSession session) {
        String resetEmail = (String) session.getAttribute("resetEmail");
        String resetUserId = (String) session.getAttribute("resetUserId");
        String newPassword = request.get("newPassword");

        if (resetEmail == null || resetUserId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "세션이 만료되었습니다. 다시 시도해주세요."));
        }

        boolean updated = userService.updatePassword(resetEmail, resetUserId, newPassword);
        if (updated) {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "비밀번호 재설정에 실패했습니다."));
        }
    }
}