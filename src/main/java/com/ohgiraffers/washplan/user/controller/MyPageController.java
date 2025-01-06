package com.ohgiraffers.washplan.user.controller;

import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.user.model.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MyPageController {

    private final UserService userService;

    public MyPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("email", userDetails.getEmail());

        return "mypage/mypage";
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = userDetails.getEmail();
        String userId = userDetails.getUsername();
        String newPassword = request.get("password");

        if (newPassword == null || newPassword.isBlank()) {
            response.put("success", false);
            response.put("message", "비밀번호는 필수입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        if (newPassword.length() < 8 || !newPassword.matches(".*\\d.*") || !newPassword.matches(".*[!@#$%^&*].*")) {
            response.put("success", false);
            response.put("message", "비밀번호는 8자리 이상이어야 하며 숫자와 특수문자를 포함해야 합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isUpdated = userService.updatePassword(email, userId, newPassword);

        if (isUpdated) {
            response.put("success", true);
            response.put("message", "비밀번호가 변경되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "비밀번호 변경에 실패했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
}
