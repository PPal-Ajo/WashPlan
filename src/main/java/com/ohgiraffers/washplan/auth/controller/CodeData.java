package com.ohgiraffers.washplan.auth.controller;

public class CodeData {
    private final String code;
    private final long expiryTime;

    public CodeData(String code, long expiryTime) {
        this.code = code;
        this.expiryTime = expiryTime;
    }

    public String getCode() {
        return code;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}