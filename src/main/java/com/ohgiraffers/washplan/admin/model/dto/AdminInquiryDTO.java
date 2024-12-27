package com.ohgiraffers.washplan.admin.model.dto;

import java.time.LocalDate;

public class AdminInquiryDTO {
    private int inquiryNo;
    private String userId;
    private String inquiryTitle;
    private LocalDate inquiryDate;
    private String replyStatus;

    public AdminInquiryDTO() {}

    public AdminInquiryDTO(int inquiryNo, String userId, String inquiryTitle, LocalDate inquiryDate, String replyStatus) {
        this.inquiryNo = inquiryNo;
        this.userId = userId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryDate = inquiryDate;
        this.replyStatus = replyStatus;
    }

    public int getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(int inquiryNo) {
        this.inquiryNo = inquiryNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInquiryTitle() {
        return inquiryTitle;
    }

    public void setInquiryTitle(String inquiryTitle) {
        this.inquiryTitle = inquiryTitle;
    }

    public LocalDate getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(LocalDate inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    @Override
    public String toString() {
        return "AdminInquiryDTO{" +
                "inquiryNo=" + inquiryNo +
                ", userId='" + userId + '\'' +
                ", inquiryTitle='" + inquiryTitle + '\'' +
                ", inquiryDate=" + inquiryDate +
                ", replyStatus='" + replyStatus + '\'' +
                '}';
    }
}
