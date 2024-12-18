package com.ohgiraffers.washplan.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /* 정적리소스에 대한 요청을 제외하겠다는 설정 (static 파일 하위) */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (개발 환경에서만)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/login", "/main", "/signup/**", "/email/**","/reservation/**", "/auth/**").permitAll() // 로그인 / 메인 허용
                            .anyRequest().authenticated() // 나머지 항목은 비허용
        );
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // 항상 세션 유지
        )




             .formLogin(form -> form
                .loginPage("/login") // 커스텀 로그인 페이지 설정
                .defaultSuccessUrl("/main") // 로그인 성공 시 리다이렉트 경로
                .permitAll()
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
