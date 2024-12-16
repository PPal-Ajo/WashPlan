package com.ohgiraffers.washplan.auth.model.service;

import com.ohgiraffers.washplan.user.model.dao.UserMapper;
import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UserMapper userMapper;

    @Autowired
    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public boolean authenticate(String userId, String password) {
        UserDTO user = userMapper.findByUserId(userId);

        // 사용자가 존재하고 비밀번호가 일치하는지 확인
        if (user != null) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(password, user.getUserPwd());
        }
        return false;
    }



}
