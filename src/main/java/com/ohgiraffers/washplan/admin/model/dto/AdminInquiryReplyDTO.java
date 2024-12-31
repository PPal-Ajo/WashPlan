package com.ohgiraffers.washplan.admin.model.dto;

public class AdminInquiryReplyDTO {
    private int adminNo;
    private int inquiryNo;
    private String replyComment;

    public AdminInquiryReplyDTO() {}

    public AdminInquiryReplyDTO(int adminNo, int inquiryNo, String replyComment) {
        this.adminNo = adminNo;
        this.inquiryNo = inquiryNo;
        this.replyComment = replyComment;
    }

    public int getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(int adminNo) {
        this.adminNo = adminNo;
    }

    public int getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(int inquiryNo) {
        this.inquiryNo = inquiryNo;
    }

    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }

    @Override
    public String toString() {
        return "AdminInquiryReplyDTO{" +
                "adminNo=" + adminNo +
                ", inquiryNo=" + inquiryNo +
                ", replyComment='" + replyComment + '\'' +
                '}';
    }
}
