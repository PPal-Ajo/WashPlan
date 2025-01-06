package com.ohgiraffers.washplan.admin.model.dto;

import java.time.LocalDate;

public class AdminNoticeDTO {
    private int noticeNo;
    private String noticeTitle;
    private LocalDate noticeDate;
    private String noticeContent;
    private int adminNo;

    public AdminNoticeDTO() {}

    public AdminNoticeDTO(int noticeNo, String noticeTitle, LocalDate noticeDate, String noticeContent, int adminNo) {
        this.noticeNo = noticeNo;
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
        this.noticeContent = noticeContent;
        this.adminNo = adminNo;
    }

    public int getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(int noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public LocalDate getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(LocalDate noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public int getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(int adminNo) {
        this.adminNo = adminNo;
    }

    @Override
    public String toString() {
        return "AdminNoticeDTO{" +
                "noticeNo=" + noticeNo +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeDate=" + noticeDate +
                ", noticeContent='" + noticeContent + '\'' +
                ", adminNo=" + adminNo +
                '}';
    }
}
