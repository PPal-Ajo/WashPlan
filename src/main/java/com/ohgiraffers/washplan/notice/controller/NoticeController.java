package com.ohgiraffers.washplan.notice.controller;

import com.ohgiraffers.washplan.notice.model.dto.NoticeDTO;
import com.ohgiraffers.washplan.notice.model.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice")
    public String noticePage(Model model) {
        List<NoticeDTO> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);
        return "faq/notice";
    }

    @GetMapping("/notice/detail/{noticeNo}")
    @ResponseBody
    public ResponseEntity<?> getNoticeDetail(@PathVariable int noticeNo) {
        try {
            NoticeDTO notice = noticeService.getNoticeDetail(noticeNo);
            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("상세 조회 중 오류가 발생했습니다.");
        }
    }
}
