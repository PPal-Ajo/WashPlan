package com.ohgiraffers.washplan.auth.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @GetMapping("/form")
    public String signupPage(HttpSession session, Model model) {
        System.out.println("세션 ID: " + session.getId());
        System.out.println("세션에 저장된 verifiedEmail: " + session.getAttribute("verifiedEmail"));

        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        model.addAttribute("verifiedEmail", verifiedEmail != null ? verifiedEmail : "");
        return "signup/signup";
    }
}
