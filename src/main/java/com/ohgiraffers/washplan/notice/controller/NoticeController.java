package com.ohgiraffers.washplan.notice.controller;

import com.ohgiraffers.washplan.notice.model.dto.NoticeDTO;
import com.ohgiraffers.washplan.notice.model.service.NoticeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    // 공지사항 상세 조회
    @GetMapping("/notice/detail")
    @ResponseBody
    public ResponseEntity<NoticeDTO> getNoticeDetailByTitle(@RequestParam("title") String title) {
        NoticeDTO noticeDetail = noticeService.getNoticeDetailByTitle(title);
        if (noticeDetail != null) {
            return ResponseEntity.ok(noticeDetail);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
