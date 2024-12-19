package com.ohgiraffers.washplan.auth.model.service;

import com.ohgiraffers.washplan.auth.model.dao.AuthMapper;
import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthMapper authMapper;

    public CustomUserDetailsService(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = authMapper.findByUserId(username);

        // 디버깅 출력
        if (user == null) {
            System.out.println("User not found in DB for username: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        System.out.println("DB에서 조회된 사용자: " + user);

        return new CustomUserDetails(user.getUserId(), user.getPassword(), user.getEmail());
    }
}