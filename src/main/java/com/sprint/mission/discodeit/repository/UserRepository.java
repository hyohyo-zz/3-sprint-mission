package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    public void create(User user);

    public User read(UUID id);

    public List<User> readByName(String name);

    public List<User> readAll();

    public User update(UUID id, User update);

    public boolean delete(UUID id);

}
