package com.ohgiraffers.washplan.user.model.dto;

import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("UserDTO")
public class UserDTO {
    private String userId;
    private String password;
    private String email;
    private String userStatus;
    private LocalDateTime createdTime;

    public UserDTO() {}

    public UserDTO(String userId, String password, String email, String userStatus, LocalDateTime createdTime) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userStatus = userStatus;
        this.createdTime = createdTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}