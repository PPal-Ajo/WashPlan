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

        // 입력받은 username 출력
        System.out.println("로그인 요청: username=" + username);

        UserDTO user = authMapper.findByUserId(username);

        // DB 조회 결과 출력
        if (user == null) {
            System.out.println("DB에서 사용자 조회 실패: username=" + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        System.out.println("DB에서 조회된 사용자 정보: " + user);

        // 사용자 정보를 반환하기 전에 확인
        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUserId(), user.getPassword(), user.getEmail());
        System.out.println("생성된 CustomUserDetails: " + customUserDetails);

        return customUserDetails;
    }
}