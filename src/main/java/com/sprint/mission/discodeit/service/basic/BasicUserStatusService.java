package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  public final UserRepository userRepository;
  public final UserStatusRepository userStatusRepository;

  @Transactional
  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)
        ));

    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new IllegalArgumentException(
          ErrorMessages.format("UserStatus", ErrorMessages.ERROR_EXISTS));
    }

    Instant lastOnlineTime = request.lastActiveAt();
    UserStatus status = new UserStatus(user, lastOnlineTime);

    //양방향 연관관계 명확히 하기위해
    user.setUserStatus(status);
    return userStatusRepository.save(status);
  }

  @Override
  public UserStatus find(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("UserStatus", ErrorMessages.ERROR_NOT_FOUND)));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll().stream()
        .toList();
  }

  @Transactional
  @Override
  public UserStatus update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("UserStatus", ErrorMessages.ERROR_NOT_FOUND)));

    userStatus.update(newLastActiveAt);
    return userStatus;
  }

  @Transactional
  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException(
                ErrorMessages.format("UserStatus", ErrorMessages.ERROR_NOT_FOUND)));
    userStatus.update(newLastActiveAt);

    return userStatus;
  }

  @Override
  public void delete(UUID id) {
    if (!userStatusRepository.existsById(id)) {
      throw new NoSuchElementException(
          ErrorMessages.format("UserStatus", ErrorMessages.ERROR_NOT_FOUND));
    }
    userStatusRepository.deleteById(id);
  }
}
