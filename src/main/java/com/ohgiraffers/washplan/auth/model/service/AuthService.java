package com.ohgiraffers.washplan.auth.model.service;

import com.ohgiraffers.washplan.auth.model.dao.AuthMapper;
import com.ohgiraffers.washplan.user.model.dao.UserMapper;
import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final AuthMapper authMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthMapper authMapper, BCryptPasswordEncoder passwordEncoder) {
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
    }

//    public void registerUser(String userId, String password, String email) {
//        String hashedPassword = passwordEncoder.encode(password);
//        System.out.println("해싱된 비밀번호: " + hashedPassword);
//        authMapper.insertUser(userId, hashedPassword, email);
//    }

    public String getPasswordByUserId(String userId) {
        return authMapper.findPasswordByUserId(userId);
    }

    public boolean authenticate(String userId, String rawPassword) {
        String storedPasswordHash = authMapper.findPasswordByUserId(userId);

        if (storedPasswordHash != null && passwordEncoder.matches(rawPassword, storedPasswordHash)) {
            // 인증 성공
            return true;
        }
        // 인증 실패
        return false;
    }


}




