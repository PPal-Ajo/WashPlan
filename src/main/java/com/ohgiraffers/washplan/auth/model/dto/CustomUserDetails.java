package com.ohgiraffers.washplan.auth.model.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String userId;
    private String password;
    private String email;

    public CustomUserDetails(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    // Spring Security가 권한 정보를 사용할 때 호출
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 정보를 반환 (예: ROLE_USER, ROLE_ADMIN 등)
        return null; // 필요에 따라 구현
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }

    public String getEmail() {
        return email; // 이메일 추가
    }
}
