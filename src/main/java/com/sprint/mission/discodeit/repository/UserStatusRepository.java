package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    public void create(UserStatus userstatus);

    public UserStatus read(UUID id);

    public List<UserStatus> readAll();

    public UserStatus update(UserStatus userstatus);

    public UserStatus delete(UUID id);

    public void deleteByUserId(UUID userId);

    public UserStatus readByUserId(UUID userId);
}
