package com.ohgiraffers.washplan.admin.model.dto;

import java.time.LocalDate;

public class AdminDTO {
    private int userNo;
    private String userId;
    private LocalDate createdTime;
    private String userStatus;
    private int cancelCount;

    public AdminDTO() {};

    public AdminDTO(int userNo, String userId, LocalDate createdTime, String userStatus, int cancelCount) {
        this.userNo = userNo;
        this.userId = userId;
        this.createdTime = createdTime;
        this.userStatus = userStatus;
        this.cancelCount = cancelCount;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDate createTime) {
        this.createdTime = createTime;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
                "userNo=" + userNo +
                ", userId='" + userId + '\'' +
                ", createdTime=" + createdTime +
                ", userStatus='" + userStatus + '\'' +
                ", cancelCount=" + cancelCount +
                '}';
    }
}


