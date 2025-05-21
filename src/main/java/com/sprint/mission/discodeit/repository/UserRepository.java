package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    public User create(User user);

    public Optional<User> find(UUID id);

    public Optional<User> findByUserName(String name);

    public List<User> findAll();

    public User update(User update);

    public boolean delete(UUID id);

}
