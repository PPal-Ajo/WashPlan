package com.ohgiraffers.washplan.auth.controller;

import com.ohgiraffers.washplan.auth.model.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;

    // AuthService 주입
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 로그인 페이지 반환
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // templates/auth/login.html 렌더링
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String userId,
                              @RequestParam String password,
                              HttpSession session) {
        boolean isAuthenticated = authService.authenticate(userId, password);

        if (isAuthenticated) {
            session.setAttribute("loggedInUser", userId);
            return "redirect:/main"; // 메인 페이지로 이동
        } else {
            return "redirect:/login?error=true"; // 로그인 페이지로 다시 이동
        }
    }

    @GetMapping("/signup/agree")
    public String showAgreementPage() {
        return "signup/agree"; // templates/signup/agree.html 렌더링
    }
}