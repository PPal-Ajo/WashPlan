package com.ohgiraffers.washplan.inquiry.model.service;

import com.ohgiraffers.washplan.inquiry.model.dao.InquiryMapper;
import com.ohgiraffers.washplan.inquiry.model.dto.InquiryDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    
    public List<InquiryDTO> getInquiriesByUserNo(int userNo) {
        List<InquiryDTO> inquiries = inquiryMapper.findInquiriesByUserNo(userNo);
        System.out.println("조회된 문의사항: " + inquiries);
        return inquiries;
    }
    
    public InquiryDTO getInquiryDetail(int inquiryNo) {
        return inquiryMapper.findInquiryByNo(inquiryNo);
    }
    
    public boolean deleteInquiry(int inquiryNo) {
        return inquiryMapper.deleteInquiry(inquiryNo) > 0;
    }
}
