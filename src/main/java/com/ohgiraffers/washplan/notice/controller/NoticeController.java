package com.ohgiraffers.washplan.notice.controller;

import com.ohgiraffers.washplan.notice.model.dto.NoticeDTO;
import com.ohgiraffers.washplan.notice.model.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.List;

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
}
