package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.handler.LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableWebSecurity
@EnableMethodSecurity
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

            // 로그아웃 설정
            .logout(logout -> logout
                // 로그아웃 처리 URL
                .logoutUrl("/api/auth/logout")
                // 로그아웃 성공 시 처리 핸들러
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                // 로그아웃 페이지는 인증없이 접근 가능
                .permitAll()
            )

            /**
            * - 인증되지 않은 사용자가 요청 시: 401 Unauthorized 응답
            * - 권한이 없는 사용자가 요청 시: 403 Forbidden 응답
            */
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler((request, response, accessDeniedException) ->
                    response.sendError(HttpServletResponse.SC_FORBIDDEN))
            )

            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 메인 페이지 및 개발 도구는 인증 불필요
                .requestMatchers("/").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()

                // 인증 없이 접근 가능한 API
                .requestMatchers("/api/auth/csrf-token").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/logout").permitAll()
                .requestMatchers("/login").permitAll()  // 커스텀 로그인 페이지 경로

                // 나머지 API는 인증 필요
                .requestMatchers("/api/**").authenticated()

                // 그 외 모든 요청은 허용
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            // 브라우저 기본 요청 및 에러 페이지
            .requestMatchers("/favicon.ico", "/error")
            .requestMatchers("/static/**", "/css/**", "/js/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
