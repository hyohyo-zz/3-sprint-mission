package com.sprint.mission.discodeit.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.DiscodeitUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        log.debug("[LoginSuccessHandler] 로그인 성공 처리 시작");

        // UserDetails에서 사용자 정보 추출
        if (authentication.getPrincipal() instanceof DiscodeitUserDetails userDetails) {
            
            // UserStatus 업데이트 (온라인 상태 갱신)
            updateUserStatus(userDetails.getUserDto().username());
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(), userDetails.getUserDto());
            log.debug("[LoginSuccessHandler] 로그인 성공 응답 완료: " + userDetails.getUsername());
        } else {
            // 예상치 못한 Principal 타입인 경우
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"인증 정보를 처리할 수 없습니다.\"}");

            log.debug("[LoginSuccessHandler] 예상치 못한 Principal 타입: " + authentication.getPrincipal().getClass());
        }
    }
    
    private void updateUserStatus(String username) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null && user.getStatus() != null) {
                user.getStatus().update(Instant.now());
                userRepository.save(user);
                log.debug("[LoginSuccessHandler] 사용자 {} 온라인 상태 업데이트 완료", username);
            }
        } catch (Exception e) {
            log.warn("[LoginSuccessHandler] 사용자 {} 상태 업데이트 중 오류: {}", username, e.getMessage());
        }
    }
}
