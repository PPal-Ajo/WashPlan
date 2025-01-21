package com.ohgiraffers.washplan.user.controller;

import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.user.model.dao.UserMapper;
import com.ohgiraffers.washplan.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/verify-password")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> verifyPassword(@RequestBody Map<String, String> request, 
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        String password = request.get("password");
        System.out.println("Received password verification request");
        
        if (userDetails == null) {
            System.out.println("UserDetails is null");
            return ResponseEntity.badRequest().body(new HashMap<>());
        }
        
        // 현재 로그인한 사용자의 비밀번호와 입력된 비밀번호 비교
        boolean isMatch = passwordEncoder.matches(password, userDetails.getPassword());
        System.out.println("Password match result: " + isMatch);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isMatch);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(userMapper.countByUserId(userId) > 0);
    }
}
