package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.handler.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.handler.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.security.jwt.handler.JwtLogoutHandler;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Profile("dev")
    @Bean
    public CommandLineRunner logFilterChains(List<SecurityFilterChain> chains) {
        return args -> {
            int chainIdx = 1;
            for (SecurityFilterChain chain : chains) {
                var filters = chain.getFilters();
                log.info("=== SecurityFilterChain #{} ({} filters) ===", chainIdx++,
                    filters.size());
                for (int i = 0; i < filters.size(); i++) {
                    String name = filters.get(i).getClass().getSimpleName();
                    log.info("  [{} / {}] {}", i + 1, filters.size(), name);
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtLoginSuccessHandler jwtLoginSuccessHandler,
        JwtLogoutHandler jwtLogoutHandler,
        JwtAuthenticationFilter jwtAuthenticationFilter,
        LoginFailureHandler loginFailureHandler,
        CustomAccessDeniedHandler customAccessDeniedHandler
    ) throws Exception {
        http
            // CSRF 설정 - 인증 관련 엔드포인트는 제외
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .ignoringRequestMatchers("/api/auth/login", "/api/auth/logout", "/api/auth/refresh")
            )

            // 로그인 설정
            .formLogin(login -> login
                .loginProcessingUrl("/api/auth/login")
                .successHandler(jwtLoginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll()
            )

            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(jwtLogoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .permitAll()
            )

            // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // 예외 처리
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler(customAccessDeniedHandler)
            )

            // JWT 기반 STATELESS 세션 관리
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                .requestMatchers("/api/auth/csrf-token").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/logout").permitAll()
                .requestMatchers("/api/auth/refresh").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/favicon.ico", "/error")
            .requestMatchers("/static/**", "/css/**", "/js/**");
    }
}