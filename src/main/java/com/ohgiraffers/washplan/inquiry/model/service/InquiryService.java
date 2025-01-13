package com.ohgiraffers.washplan.inquiry.model.service;

import com.ohgiraffers.washplan.inquiry.model.dao.InquiryMapper;
import com.ohgiraffers.washplan.inquiry.model.dto.InquiryDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InquiryService {
    
    private final InquiryMapper inquiryMapper;
    
    public InquiryService(InquiryMapper inquiryMapper) {
        this.inquiryMapper = inquiryMapper;
    }
    
    @Transactional
    public boolean createInquiry(InquiryDTO inquiry) {
        return inquiryMapper.insertInquiry(inquiry) > 0;
    }
}
