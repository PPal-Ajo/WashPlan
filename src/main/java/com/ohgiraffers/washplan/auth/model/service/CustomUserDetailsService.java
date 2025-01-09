package com.ohgiraffers.washplan.auth.model.service;

import com.ohgiraffers.washplan.admin.model.dto.AdminAuthDTO;
import com.ohgiraffers.washplan.auth.model.dao.AuthMapper;
import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import com.ohgiraffers.washplan.admin.model.dao.AdminMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthMapper authMapper;
    private final AdminMapper adminMapper;

    public CustomUserDetailsService(AuthMapper authMapper, AdminMapper adminMapper) {
        this.authMapper = authMapper;
        this.adminMapper = adminMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TBL_USER 조회
        UserDTO user = authMapper.findUserByUserId(username);
        if (user != null) {
            // 사용자 상태 확인 (adminMapper 사용)
            String userStatus = adminMapper.getUserStatus(username);
            if ("일시정지".equals(userStatus)) {
                throw new LockedException("계정이 일시정지 상태입니다.");
            }
            if ("영구탈퇴".equals(userStatus)) {
                throw new DisabledException("계정이 영구탈퇴 상태입니다.");
            }

            return new CustomUserDetails(
                    user.getUserNo(),
                    user.getUserId(),
                    user.getPassword(),
                    user.getEmail(),
                    "ROLE_USER"
            );
        }

        // TBL_ADMIN 조회
        AdminAuthDTO admin = authMapper.findAdminByAdminId(username);
        if (admin != null) {
            return new CustomUserDetails(
                    admin.getAdminNo(),
                    admin.getAdminId(),
                    admin.getAdminPwd(),
                    null, // 관리자에는 이메일이 없으므로 null
                    "ROLE_ADMIN"
            );
        }

        throw new UsernameNotFoundException("User or Admin not found: " + username);
    }
}
