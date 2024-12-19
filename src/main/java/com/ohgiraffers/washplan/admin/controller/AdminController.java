package com.ohgiraffers.washplan.admin.controller;


import com.ohgiraffers.washplan.reservation.model.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final ReservationService reservationService;

    @Autowired
    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {

        return "admin/admin";
    }
    @GetMapping("/adminUser")
    public String adminUser(Model model) {

        return "admin/adminUser";
    }
    @GetMapping("/adminMachine")
    public String adminMachine(Model model) {

        return "admin/adminMachine";
    }
    @GetMapping("/adminInquiry")
    public String adminInquiry(Model model) {

        return "admin/adminInquiry";
    }
}
