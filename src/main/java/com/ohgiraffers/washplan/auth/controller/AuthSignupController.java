package com.ohgiraffers.washplan.auth.controller;

import com.ohgiraffers.washplan.auth.model.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthSignupController {

    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    // AuthService와 passwordEncoder 주입
    @Autowired
    public AuthSignupController(AuthService authService, BCryptPasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String userId,
                              @RequestParam String password,
                              HttpSession session) {
        // DB에서 비밀번호 가져오기
        String storedPasswordHash = authService.getPasswordByUserId(userId);

        // 디버깅 로그
        if (storedPasswordHash != null) {
            System.out.println("DB에서 가져온 해시된 비밀번호: " + storedPasswordHash);
            System.out.println("입력한 비밀번호: " + password);
            if (passwordEncoder.matches(password, storedPasswordHash)) {
                System.out.println("비밀번호 검증 성공!");
            } else {
                System.out.println("비밀번호 검증 실패!");
            }
        } else {
            System.out.println("사용자를 찾을 수 없습니다.");
        }

        if (storedPasswordHash != null && passwordEncoder.matches(password, storedPasswordHash)) {
            session.setAttribute("loggedInUser", userId);
            return "redirect:/main";
        } else {
            return "redirect:/login?error=true";
        }
    }




    @GetMapping("/signup/agree")
    public String showAgreementPage() {
        return "signup/agree";
    }

    @GetMapping("/signup/email")
    public String showEmailPage() {
        return "signup/email";
    }

    @GetMapping("/signup/signup")
    public String singUpPage() {
        return "signup/signup";
    }

    @GetMapping("/forget")
    public String forgetPage() { return "auth/forget";}

    @GetMapping("/findid")
    public String findIdPage() { return "auth/findid";}

    @GetMapping("/findpwd")
    public String findPwdPage() { return "auth/findpwd";}

    @GetMapping("/resetpwd")
    public String resetPwdPage() { return "auth/reset";}
}