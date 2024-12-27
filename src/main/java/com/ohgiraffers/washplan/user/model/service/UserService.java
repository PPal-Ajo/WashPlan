package com.ohgiraffers.washplan.user.model.service;


import com.ohgiraffers.washplan.user.model.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isEmailDuplicate(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    public boolean checkEmailExistence(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    public String getUserIdByEmail(String email) {
        return userMapper.getUserIdByEmail(email);
    }

    public boolean checkEmailAndId(String email, String userId) {
        return userMapper.existsByEmailAndId(email, userId);
    }

    public boolean validateCredentials(String email, String userId) {
        return userMapper.existsByEmailAndId(email, userId);
    }


    public boolean updatePassword(String email, String userId, String newPassword) {
        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(newPassword);
        return userMapper.updatePasswordByEmailAndId(email, userId, hashedPassword) > 0;
    }
}
