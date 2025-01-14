package com.ohgiraffers.washplan.inquiry.model.dao;

import com.ohgiraffers.washplan.inquiry.model.dto.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InquiryMapper {
    int insertInquiry(InquiryDTO inquiry);
    List<InquiryDTO> findInquiriesByUserNo(int userNo);
}