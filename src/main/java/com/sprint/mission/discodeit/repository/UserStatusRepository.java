package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    public UserStatus create(UserStatus userStatus);

    public Optional<UserStatus> find(UUID id);

    public Optional<UserStatus> findByUserId(UUID userId);

    public List<UserStatus> findAll();

    public boolean existsById(UUID id);

    public void deleteById(UUID id);

    public void deleteByUserId(UUID userId);
}
