package com.ohgiraffers.washplan.auth.controller;

import com.ohgiraffers.washplan.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String, Boolean>> checkCredentials(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String userId = request.get("userId");

        boolean isValid = userService.checkEmailAndId(email, userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isValid);
        return ResponseEntity.ok(response);
    }
}
