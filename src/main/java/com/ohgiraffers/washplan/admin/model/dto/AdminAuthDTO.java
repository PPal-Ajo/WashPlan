package com.ohgiraffers.washplan.admin.model.dto;

public class AdminAuthDTO {

    private int adminNo;
    private String adminId;
    private String adminPwd;

    public AdminAuthDTO() {}

    public AdminAuthDTO(int adminNo, String adminId, String adminPwd) {
        this.adminNo = adminNo;
        this.adminId = adminId;
        this.adminPwd = adminPwd;
    }

    public int getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(int adminNo) {
        this.adminNo = adminNo;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminPwd() {
        return adminPwd;
    }

    public void setAdminPwd(String adminPwd) {
        this.adminPwd = adminPwd;
    }

    @Override
    public String toString() {
        return "AdminAuthDTO{" +
                "adminNo=" + adminNo +
                ", adminId='" + adminId + '\'' +
                ", adminPwd='" + adminPwd + '\'' +
                '}';
    }
}
