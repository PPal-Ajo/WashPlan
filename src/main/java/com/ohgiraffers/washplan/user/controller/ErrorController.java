package com.ohgiraffers.washplan.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/403")
    public String error403(Model model) {

        return "error/error403";
    }

    @GetMapping("/error/404")
    public String error404(Model model) {

        return "error/error404";
    }

    @GetMapping("/error/500")
    public String error500(Model model) {

        return "error/error500";
    }
}
