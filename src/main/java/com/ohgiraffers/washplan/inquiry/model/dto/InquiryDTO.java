package com.ohgiraffers.washplan.inquiry.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InquiryDTO {
    private int inquiryNo;
    private int userNo;
    private String inquiryTitle;
    private String inquiryContent;
    private LocalDate inquiryDate;
    private String replyStatus;
    private String replyComment;

    public InquiryDTO() {}

    public InquiryDTO(int userNo, String inquiryTitle, String inquiryContent) {
        this.userNo = userNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryDate = LocalDate.now();
        this.replyStatus = "대기중";
    }

    // Getters and Setters
    public int getInquiryNo() { return inquiryNo; }
    public void setInquiryNo(int inquiryNo) { this.inquiryNo = inquiryNo; }
    public int getUserNo() { return userNo; }
    public void setUserNo(int userNo) { this.userNo = userNo; }
    public String getInquiryTitle() { return inquiryTitle; }
    public void setInquiryTitle(String inquiryTitle) { this.inquiryTitle = inquiryTitle; }
    public String getInquiryContent() { return inquiryContent; }
    public void setInquiryContent(String inquiryContent) { this.inquiryContent = inquiryContent; }
    public LocalDate getInquiryDate() { return inquiryDate; }
    public void setInquiryDate(LocalDate inquiryDate) { this.inquiryDate = inquiryDate; }
    public String getReplyStatus() { return replyStatus; }
    public void setReplyStatus(String replyStatus) { this.replyStatus = replyStatus; }
    public String getReplyComment() {
        return replyComment;
    }
    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }
}
