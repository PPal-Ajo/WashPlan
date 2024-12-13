package com.ohgiraffers.washplan.user.model.dto;

public class UserDTO {
    private String userNo;
    private String userId;
    private String userPwd;
    private String email;
    private String userStatus;

    public UserDTO() {}

    public UserDTO(String userNo, String userId, String userPwd, String email, String userStatus) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPwd = userPwd;
        this.email = email;
        this.userStatus = userStatus;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
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
}
