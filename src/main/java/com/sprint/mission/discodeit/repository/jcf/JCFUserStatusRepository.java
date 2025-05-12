package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.time.Instant;
import java.util.*;

public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data = new HashMap<>();

    @Override
    public UserStatus create(UserStatus userstatus) {
        this.data.put(userstatus.getUserId(), userstatus);
        return userstatus;
    }

    @Override
    public UserStatus find(UUID id) {
        return this.data.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(userStatus -> Objects.equals(userStatus.getUserId(), userId))
                .findFirst();
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public boolean deleteByUserId(UUID userId) {
        return data.remove(userId) != null;
    }
}
