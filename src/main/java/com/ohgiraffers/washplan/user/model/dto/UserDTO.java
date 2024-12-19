package com.ohgiraffers.washplan.user.model.dto;

import java.util.Date;

public class UserDTO {
    private String userId;
    private String userPwd;
    private String email;
    private String userStatus;
    private Date createdTime;

    public UserDTO() {}


    public UserDTO(String userId, String userPwd, String email, String userStatus, Date createdTime) {
        this.userId = userId;
        this.userPwd = userPwd;
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

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
