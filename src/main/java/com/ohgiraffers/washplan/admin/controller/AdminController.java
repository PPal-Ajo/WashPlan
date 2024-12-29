package com.ohgiraffers.washplan.admin.controller;


import com.ohgiraffers.washplan.admin.model.dto.AdminDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminInquiryDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminMachineDTO;
import com.ohgiraffers.washplan.admin.model.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/admin/adminuser")
    public String adminuser(Model model) {

        return "admin/adminuser";
    }

    // JSON 데이터 반환
    @GetMapping("/adminuser/data")
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

    @GetMapping("/adminuser/search")
    @ResponseBody
    public List<AdminDTO> searchUsers(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {
        if ("전체".equals(category)) {
            return adminService.searchAll(keyword);
        } else if ("자동취소".equals(category)) {
            int cancelCount = Integer.parseInt(keyword);
            return adminService.searchByCancelCount(cancelCount);
        } else {
            return new ArrayList<>(); // 빈 리스트 반환
        }
    }


    @GetMapping("/admin/adminmachine")
    public String adminMachine(Model model) {


        return "admin/adminmachine";
    }

    @GetMapping("/adminmachine/wash")
    @ResponseBody
    public List<AdminMachineDTO> getWashMachineData() {

        return adminService.getWashMachineInfo();
    }
    @GetMapping("/adminmachine/dry")
    @ResponseBody
    public List<AdminMachineDTO> getDryMachineData() {

        return adminService.getDryMachineInfo();
    }
    @PostMapping("/adminmachine/changeStatus")
    @ResponseBody
    public ResponseEntity<Void> changeMachineStatus(@RequestBody List<Integer> machineNos) {
        adminService.changeMachineStatus(machineNos);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/admininquiry")
    public String adminInquiry(Model model) {

        return "admin/admininquiry";
    }

    // 문의사항 데이터를 JSON 형태로 반환하는 API
    @GetMapping("/admininquiry/inquiries")
    @ResponseBody
    public ResponseEntity<List<AdminInquiryDTO>> getInquiries() {
        List<AdminInquiryDTO> inquiries = adminService.getAllInquiries();
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/admininquiry/search")
    @ResponseBody
    public ResponseEntity<List<AdminInquiryDTO>> searchInquiries(
            @RequestParam String category,
            @RequestParam String query) {

        List<AdminInquiryDTO> inquiries;

        if (category.equals("전체")) {
            inquiries = adminService.searchInquiriesByUserIdOrTitle(query);
        } else if (category.equals("답변")) {
            inquiries = adminService.searchInquiriesByReplyStatus(query);
        } else {
            inquiries = new ArrayList<>();
        }

        return ResponseEntity.ok(inquiries);
    }

    @PostMapping("/admininquiry/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteInquiries(@RequestBody List<Integer> inquiryNos) {
        adminService.deleteInquiries(inquiryNos);
        return ResponseEntity.ok().build();
    }


}
