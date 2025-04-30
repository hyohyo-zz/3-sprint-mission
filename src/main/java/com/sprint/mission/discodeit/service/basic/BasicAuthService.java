package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;

public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    public BasicAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(LoginRequest request) {
        //1. Username으로 user 찾기
        User user = userRepository. findByUserName(request.userName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        //2. password 일치 여부 확인
        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }
}
