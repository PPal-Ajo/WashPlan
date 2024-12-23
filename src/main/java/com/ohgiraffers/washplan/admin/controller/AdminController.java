package com.ohgiraffers.washplan.admin.controller;


import com.ohgiraffers.washplan.admin.model.dto.AdminDTO;
import com.ohgiraffers.washplan.admin.model.service.AdminService;
import com.ohgiraffers.washplan.reservation.model.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {

        return "admin/admin";
    }

    @GetMapping("/adminUser")
    public String adminUser(Model model) {

        return "admin/adminUser";
    }

    // JSON 데이터 반환
    @GetMapping("/adminUser/data")
    @ResponseBody
    public List<AdminDTO> getAllUsersData() {
        return adminService.getAllUsers(); // 사용자 데이터를 JSON 형식으로 반환
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseUsers(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> userNos = request.get("userNos");

        if (userNos == null || userNos.isEmpty()) {
            return ResponseEntity.badRequest().body("No users selected for pause.");
        }

        adminService.pauseUsers(userNos);
        return ResponseEntity.ok("일시정지 완료");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUsers(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> userNos = request.get("userNos");
        adminService.deleteUsers(userNos);
        return ResponseEntity.ok("영구탈퇴 완료");
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
