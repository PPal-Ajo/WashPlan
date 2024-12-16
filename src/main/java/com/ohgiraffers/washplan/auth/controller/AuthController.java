package com.ohgiraffers.washplan.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {



    // 로그인 페이지 반환
    @GetMapping("login")
    public String loginPage() {
        return "auth/login"; // templates/auth/login.html 렌더링
    }

}