package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.UserStatusResponse;
import com.sprint.mission.discodeit.dto.request.create.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    public final UserRepository userRepository;
    public final UserStatusRepository userStatusRepository;


    @Override
    public UserStatusResponse create(UserStatusCreateRequest request) {
        User user = userRepository.find(request.userId())
                .orElseThrow(() -> new IllegalArgumentException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );

        Optional<UserStatus> existing = userStatusRepository.findByUserId(request.userId());
        if(existing.isPresent()) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("UserStatus", ErrorMessages.ERROR_EXISTS)
            );
        }

        UserStatus status = new UserStatus(request.userId(), Instant.now());
        userStatusRepository.create(status);
        return toUserStatusResponse(status);
    }

    @Override
    public UserStatusResponse find(UUID id) {
        UserStatus userStatus = userStatusRepository.find(id);
        if(userStatus == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)
            );
        }
        return toUserStatusResponse(userStatus);
    }

    @Override
    public List<UserStatusResponse> findAll() {
        return userStatusRepository.findAll().stream()
                .map(this::toUserStatusResponse)
                .toList();
    }

    @Override
    public UserStatusResponse update(UUID userStatusId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.find(userStatusId);
        if(userStatus == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("UserStatus", ErrorMessages.ERROR_NOT_FOUND));
        }

        userStatus.updatelastOnline(Instant.now());
        UserStatus updateUserStatus = userStatusRepository.create(userStatus);
        return toUserStatusResponse(updateUserStatus);
    }

    @Override
    public UserStatusResponse updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Optional<UserStatus> optionalUserStatus = userStatusRepository.findByUserId(userId);

        if (optionalUserStatus.isEmpty()) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("UserStatus", ErrorMessages.ERROR_NOT_FOUND));
        }

        UserStatus userStatus = optionalUserStatus.get();
        userStatus.updatelastOnline(Instant.now());
        UserStatus updateUserStatus = userStatusRepository.create(userStatus);
        return toUserStatusResponse(updateUserStatus);
    }

    @Override
    public boolean delete(UUID id) {
        UserStatus userStatus = userStatusRepository.find(id);
        if(userStatus == null) {
            throw new IllegalArgumentException(ErrorMessages.format(
                    "UserStatus", ErrorMessages.ERROR_NOT_FOUND));
        }
        userStatusRepository.delete(id);
        return true;
    }

    private UserStatusResponse toUserStatusResponse(UserStatus status) {
        return new UserStatusResponse(
                status.getUserId(),
                status.isOnline(),
                status.getLastOnlineTime()
        );
    }
}
