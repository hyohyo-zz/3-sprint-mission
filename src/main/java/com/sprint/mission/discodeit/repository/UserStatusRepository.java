package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    public UserStatus create(UserStatus userstatus);

    public UserStatus find(UUID id);

    public List<UserStatus> findAll();

    public Optional<UserStatus> findByUserId(UUID userId);

    public UserStatus update(UserStatus userstatus);

    public boolean delete(UUID id);

    public boolean deleteByUserId(UUID userId);
}
