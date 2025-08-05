package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        LoginSuccessHandler loginSuccessHandler,
        LoginFailureHandler loginFailureHandler

    ) throws Exception {
        http
            // CSRF 설정 - 쿠키 기반 CSRF 토큰 사용
            .csrf(csrf -> csrf
                // 쿠키 기반 CSRF 토큰 사용
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // CSRF 토큰 요청 처리 핸들러 설정
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            )

            // Form 기반 로그인 활성화
            .formLogin(login -> login
                // 로그인 처리 URL
                .loginProcessingUrl("/api/auth/login")
                // 로그인 성공 시 처리 핸들러
                .successHandler(loginSuccessHandler)
                // 로그인 실패 시 처리 핸들러
                .failureHandler(loginFailureHandler)
                // 로그인 페이지는 인증 없이 접근 가능
                .permitAll()
            )

            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/me").authenticated()
                .anyRequest().permitAll()
            )

            // 로그아웃 설정
            .logout(logout -> logout
                // 로그아웃 처리 URL
                .logoutUrl("/api/auth/logout")
                // 로그아웃 성공 시 처리 핸들러
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                // 로그아웃 페이지는 인증없이 접근 가능
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
