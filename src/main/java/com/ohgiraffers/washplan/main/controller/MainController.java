package com.ohgiraffers.washplan.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/","/main"})
    public String main() {
        return "main/main";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq/faq";
    }
}
