package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public UserDto login(LoginRequest request) {
        log.info("[로그인] 요청: {}", request);

        String userName = request.username();
        String password = request.password();

        //1. userName 으로 user 찾기
        User user = userRepository.findByUsername(userName)
            .orElseThrow(() -> {
                log.warn("[로그인 실패] 존재하지 않는 사용자: {}", userName);

                return new NoSuchElementException(
                    ErrorMessages.format("user", ErrorMessages.ERROR_NOT_FOUND));
            });

        //2. password 일치 여부 확인
        if (!user.getPassword().equals(password)) {
            log.warn("[로그인 실패] 비밀번호 불일치. 입력값: {}", password);

            throw new IllegalArgumentException(
                ErrorMessages.format("password", ErrorMessages.ERROR_MISMATCH));
        }

        log.info("[로그인 성공] userId={}, userName={}", user.getId(), user.getUsername());

        return userMapper.toDto(user);
    }
}
