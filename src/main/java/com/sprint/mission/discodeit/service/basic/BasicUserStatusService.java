package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.create.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    public final UserRepository userRepository;
    public final UserStatusRepository userStatusRepository;


    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        User user = userRepository.find(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Optional<UserStatus> existing = userStatusRepository.findByUserId(request.userId());
        if(existing.isPresent()) {
            throw new IllegalArgumentException("해당 유저에 대한 상태 정보가 이미 존재");
        }

        UserStatus status = new UserStatus(request.userId(), request.isOnline());
        userStatusRepository.create(status);
        return status;
    }

    @Override
    public UserStatus find(UUID id) {
        UserStatus userStatus = userStatusRepository.find(id);
        if(userStatus == null) {
            throw new IllegalArgumentException("해당 Id의 UserStatus를 찾을 수 없습니다.");
        }
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.find(request.id());
        if(userStatus == null) {
            throw new IllegalArgumentException("수정할 UserStatus가 존재하지 않습니다.");
        }

        userStatus.updateOnlineStatus(request.newOnlineStatus());
        return userStatusRepository.update(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Optional<UserStatus> optionalUserStatus = userStatusRepository.findByUserId(userId);

        if (optionalUserStatus.isEmpty()) {
            throw new IllegalArgumentException("해당 유저에 대한 UserStatus가 존재하지 않습니다.");
        }

        UserStatus userStatus = optionalUserStatus.get();
        userStatus.updateOnlineStatus(request.newOnlineStatus());
        return userStatusRepository.update(userStatus);

    }

    @Override
    public boolean delete(UUID id) {
        UserStatus userStatus = userStatusRepository.find(id);
        if(userStatus == null) {
            throw new IllegalArgumentException("삭제할 UserStatus가 존재하지 않습니다.");
        }
        userStatusRepository.delete(id);
        return true;
    }
}
