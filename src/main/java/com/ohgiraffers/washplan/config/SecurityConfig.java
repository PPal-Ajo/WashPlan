package com.ohgiraffers.washplan.config;

import com.ohgiraffers.washplan.auth.model.service.CustomUserDetailsService;
import com.ohgiraffers.washplan.admin.model.dao.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;

@Configuration
public class SecurityConfig {

    private final AdminMapper adminMapper;

    @Autowired
    public SecurityConfig(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 운영 환경에서 활성화 필요
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 리소스 허용
                        .requestMatchers("/login", "/signup/**", "/email/**", "/forget/**", "/findid/**", "/findpwd/**", "/resetpwd/**", "/auth/**", "/main", "/","/api/**","/error/**","/machine/**").permitAll() // 비회원 허용 경로
                        .requestMatchers("/reservation/**", "/mypage/**", "change-password","/faq/**", "/faq/api/**").hasRole("USER") // 예약 페이지는 USER 권한 필요
                        .requestMatchers("/admin/**", "/adminUser/**", "/adminMachine/**", "/adminInquiry/**").hasRole("ADMIN") // 관리자 페이지는 ADMIN 권한 필요
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .anonymous(anonymous -> anonymous
                        .authorities("ROLE_ANONYMOUS")
                )// 익명 사용자 권한 지정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요 시 세션 생성
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/main") // 기본 성공 URL
                        .successHandler((request, response, authentication) -> {
                            // 권한 확인
                            if (authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                response.sendRedirect("/admin"); // 관리자인 경우 관리자 페이지로
                            } else {
                                response.sendRedirect("/main"); // 일반 사용자인 경우 메인 페이지로
                            }
                        })
                        .failureHandler((request, response, exception) -> {
                            try {
                                String username = request.getParameter("username");
                                String status = adminMapper.getUserStatus(username);
                                
                                if ("일시정지".equals(status)) {
                                    LocalDate endDate = adminMapper.getPenaltyEndDate(username);
                                    response.sendRedirect("/login?error&status=pause&endDate=" + 
                                            (endDate != null ? endDate.toString() : ""));
                                } else if ("영구탈퇴".equals(status)) {
                                    response.sendRedirect("/login?error&status=permanent");
                                } else {
                                    response.sendRedirect("/login?error");
                                }
                            } catch (Exception e) {
                                response.sendRedirect("/login?error");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}