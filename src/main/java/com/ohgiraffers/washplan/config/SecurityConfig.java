package com.ohgiraffers.washplan.config;

import com.ohgiraffers.washplan.auth.model.service.CustomUserDetailsService;
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


@Configuration
public class SecurityConfig {

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
                        .requestMatchers("/login", "/signup/**", "/email/**", "/forget/**", "/findid/**", "/findpwd/**", "/resetpwd/**", "/auth/**", "/main", "/","/api/**").permitAll() // 비회원 허용 경로
                        .requestMatchers("/reservation/**", "/mypage/**").hasRole("USER") // 예약 페이지는 USER 권한 필요
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
                            // 로그인 성공 후 처리
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            if ("ROLE_ADMIN".equals(role)) {
                                // 어드민 권한일 경우 관리자 페이지로 리다이렉트
                                response.sendRedirect("/admin");
                            } else if ("ROLE_USER".equals(role)) {
                                // 유저 권한일 경우 메인 페이지로 리다이렉트
                                response.sendRedirect("/main");
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("로그인 실패: " + exception.getMessage());
                            response.sendRedirect("/login?error");
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