package com.ohgiraffers.washplan.user.controller;

import com.ohgiraffers.washplan.user.model.dao.UserMapper;
import com.ohgiraffers.washplan.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam("userId") String userId) {
        return userMapper.countByUserId(userId) > 0;
    }
}
