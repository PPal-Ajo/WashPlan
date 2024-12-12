package com.ohgiraffers.washplan.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class AuthController {

    @GetMapping("login")
    public String loginPage() {
        return "auth/login"; // templates/auth/login.html로 매핑


    }
}


