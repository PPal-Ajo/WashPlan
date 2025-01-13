package com.ohgiraffers.washplan.inquiry.model.dao;

import com.ohgiraffers.washplan.inquiry.model.dto.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryMapper {
    int insertInquiry(InquiryDTO inquiry);
}
