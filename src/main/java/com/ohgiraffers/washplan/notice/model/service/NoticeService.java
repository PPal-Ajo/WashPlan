package com.ohgiraffers.washplan.notice.model.service;

import com.ohgiraffers.washplan.notice.model.dao.NoticeMapper;
import com.ohgiraffers.washplan.notice.model.dto.NoticeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    
    private final NoticeMapper noticeMapper;
    
    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }
    
    public List<NoticeDTO> getAllNotices() {
        return noticeMapper.findAllNotices();
    }
}
