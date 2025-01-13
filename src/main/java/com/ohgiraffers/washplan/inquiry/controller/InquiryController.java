package com.ohgiraffers.washplan.inquiry.controller;

import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.washplan.inquiry.model.service.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
    
    private final InquiryService inquiryService;
    
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/create")
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
}
