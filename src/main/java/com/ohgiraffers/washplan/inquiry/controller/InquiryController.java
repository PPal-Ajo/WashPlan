package com.ohgiraffers.washplan.inquiry.controller;

import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.washplan.inquiry.model.service.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class InquiryController {
    
    private final InquiryService inquiryService;
    
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/inquiry/create")
    @ResponseBody
    public ResponseEntity<?> createInquiry(@RequestBody Map<String, String> request) {
        try {
            // 현재 로그인한 사용자 정보 가져오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            
            InquiryDTO inquiry = new InquiryDTO(
                userDetails.getUserNo(),
                request.get("title"),
                request.get("content")
            );
            
            boolean result = inquiryService.createInquiry(inquiry);
            
            if(result) {
                return ResponseEntity.ok().body("문의가 등록되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("문의 등록에 실패했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("문의 등록 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/mypage/inquiry")
    public String getMyInquiries(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            
            int userNo = userDetails.getUserNo();
            System.out.println("현재 로그인한 사용자 번호: " + userNo);
            
            List<InquiryDTO> inquiries = inquiryService.getInquiriesByUserNo(userNo);
            System.out.println("조회된 문의사항 수: " + (inquiries != null ? inquiries.size() : 0));
            
            model.addAttribute("inquiries", inquiries);
            return "mypage/inquiry";
        } catch (Exception e) {
            System.out.println("에러 발생: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/inquiry/detail/{inquiryNo}")
    @ResponseBody
    public ResponseEntity<?> getInquiryDetail(@PathVariable int inquiryNo) {
        try {
            InquiryDTO inquiry = inquiryService.getInquiryDetail(inquiryNo);
            return ResponseEntity.ok(inquiry);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("상세 조회 중 오류가 발생했습니다.");
        }
    }
}
