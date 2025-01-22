package com.ohgiraffers.washplan.admin.controller;


import com.ohgiraffers.washplan.admin.model.dto.*;
import com.ohgiraffers.washplan.admin.model.service.AdminService;
import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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

    @PostMapping("/activate")
    public ResponseEntity<String> activeUsers(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> userNos = request.get("userNos");
        adminService.activeUsers(userNos);
        return ResponseEntity.ok("활성화 완료");
    }

    @GetMapping("/adminuser/search")
    @ResponseBody
    public List<AdminDTO> searchUsers(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {
        switch(category) {
            case "검색":
                return adminService.searchAll(keyword);
            case "아이디":
                return adminService.searchById(keyword);
            case "상태":
                return adminService.searchByStatus(keyword);
            case "자동취소":
                try {
                    int cancelCount = Integer.parseInt(keyword);
                    return adminService.searchByCancelCount(cancelCount);
                } catch (NumberFormatException e) {
                    return new ArrayList<>();
                }
            default:
                return new ArrayList<>();
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

        switch(category) {
            case "검색":
                inquiries = adminService.findAllInquiries();  // 전체 조회로 변경
                break;
            case "아이디":
                inquiries = adminService.searchInquiriesByUserId(query);
                break;
            case "제목":
                inquiries = adminService.searchInquiriesByTitle(query);
                break;
            case "답변":
                inquiries = adminService.searchInquiriesByReplyStatus(query);
                break;
            default:
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

    @GetMapping("/admininquiry/detail/{inquiryNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getInquiryDetail(@PathVariable int inquiryNo) {
        AdminInquiryDTO inquiryDetail = adminService.findInquiryDetailByNo(inquiryNo);
        String replyComment = adminService.getReplyCommentByInquiryNo(inquiryNo);

        Map<String, Object> response = new HashMap<>();
        response.put("inquiryDetail", inquiryDetail);
        response.put("replyComment", replyComment); // 답변 내용 포함

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admininquiry/reply")
    @ResponseBody
    public ResponseEntity<Void> saveReply(@RequestBody AdminInquiryReplyDTO replyDTO) {
        adminService.saveReply(replyDTO);
        adminService.updateReplyStatus(replyDTO.getInquiryNo(), "완료"); // 상태 업데이트
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/adminnotice")
    public String adminNotice(Model model) {

        return "admin/adminnotice";
    }

    @GetMapping("/adminnotice/notices")
    @ResponseBody
    public ResponseEntity<List<AdminNoticeDTO>> getNotices() {
        List<AdminNoticeDTO> notices = adminService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/adminnotice/search")
    @ResponseBody
    public ResponseEntity<List<AdminNoticeDTO>> searchNotices(@RequestParam String title) {
        List<AdminNoticeDTO> notices = adminService.searchNoticesByTitle(title);
        return ResponseEntity.ok(notices);
    }

    @PostMapping("/adminnotice/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteNotices(@RequestBody List<Integer> noticeNos) {
        adminService.deleteNotices(noticeNos);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/adminnotice/write")
    @ResponseBody
    public ResponseEntity<AdminNoticeDTO> writeNotice(@RequestBody AdminNoticeDTO noticeDTO) {
        // 로그인한 유저의 번호 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            int adminNo = userDetails.getUserNo();
            noticeDTO.setAdminNo(adminNo); // 유저 번호 설정
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증되지 않은 경우 처리
        }
        adminService.insertNotice(noticeDTO);
        // 새로 추가된 공지사항 데이터 반환
        AdminNoticeDTO savedNotice = adminService.findLastAddedNotice(noticeDTO);
        return ResponseEntity.ok(savedNotice); // JSON 응답 반환
    }

    @GetMapping("/adminnotice/{noticeNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNotice(@PathVariable int noticeNo) {
        AdminNoticeDTO noticeDetail = adminService.getNoticeById(noticeNo);

        Map<String, Object> response = new HashMap<>();
        response.put("noticeDetail", noticeDetail);

        return ResponseEntity.ok(response);
    }


    // 공지사항 수정
    @PutMapping("/adminnotice/update")
    @ResponseBody
    public ResponseEntity<String> updateNotice(@RequestBody AdminNoticeDTO noticeDTO) {
        adminService.updateNotice(noticeDTO);
        return ResponseEntity.ok("공지사항이 성공적으로 수정되었습니다.");
    }

    @GetMapping("/admin/chat")
    public String chatbot() {
        return "admin/chatbot";
    }

    @PostMapping("/admin/chat/message")
    @ResponseBody
    public Map<String, String> handleChatMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = adminService.processMessage(message);
        
        Map<String, String> result = new HashMap<>();
        result.put("message", response);
        return result;
    }

    @PostMapping("/adminmachine/add")
    @ResponseBody
    public ResponseEntity<AdminMachineDTO> addMachine(@RequestBody AdminMachineDTO machine) {
        try {
            adminService.addMachine(machine);
            return ResponseEntity.ok(machine);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/adminmachine/delete")
    @ResponseBody
    public ResponseEntity<String> deleteMachines(@RequestBody List<Integer> machineNos) {
        try {
            adminService.deleteMachines(machineNos);
            return ResponseEntity.ok("기기 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("기기 삭제 실패");
        }
    }

}
