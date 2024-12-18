package com.ohgiraffers.washplan.user.model.service;


import com.ohgiraffers.washplan.user.model.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public boolean isEmailDuplicate(String email) {
        return userMapper.countByEmail(email) > 0;
    }
}
