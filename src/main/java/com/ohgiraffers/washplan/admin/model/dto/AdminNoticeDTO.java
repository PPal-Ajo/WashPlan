package com.ohgiraffers.washplan.admin.model.dto;

import java.time.LocalDate;

public class AdminNoticeDTO {
    private int noticeNo;
    private String noticeTitle;
    private LocalDate noticeDate;
    private String noticeContent;

    public AdminNoticeDTO() {}

    public AdminNoticeDTO(int noticeNo, String noticeTitle, LocalDate noticeDate, String noticeContent) {
        this.noticeNo = noticeNo;
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
        this.noticeContent = noticeContent;
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

    @Override
    public String toString() {
        return "AdminNoticeDTO{" +
                "noticeNo=" + noticeNo +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeDate=" + noticeDate +
                ", noticeContent='" + noticeContent + '\'' +
                '}';
    }
}
