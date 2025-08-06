package com.sprint.mission.discodeit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 설정 클래스.
 *
 * <p>애플리케이션 전역에서 사용할 {@link UserStatusUpdateInterceptor}를 등록한다.</p>
 *
 * <ul>
 *   <li>모든 `/api/**` 요청에 대해 인터셉터가 동작한다.</li>
 *   <li>단, 로그인, 로그아웃, CSRF 토큰 발급 요청은 제외한다.</li>
 * </ul>
 *
 * <p>이 설정을 통해 인증된 사용자의 마지막 활동 시각이 자동으로 업데이트된다.</p>
 */
@Configuration
@RequiredArgsConstructor
public class WepMvcConfig implements WebMvcConfigurer {

    private final UserStatusUpdateInterceptor userStatusUpdateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userStatusUpdateInterceptor)
            .addPathPatterns("/api/**")     // 모든 API 요청에 적용
            .excludePathPatterns("/api/auth/login", "/api/auth/logout", "/api/auth/csrf-token");  // 로그인 관련 제외
    }
}
