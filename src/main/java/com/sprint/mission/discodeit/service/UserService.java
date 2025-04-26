package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.*;

public interface UserService {

    public void create(UserCreateRequest request);

    public User read(UUID id);

    public List<User> readByName(String name);

    public List<User> readAll();

    public User update(UUID id, User update);

    public boolean delete(UUID id, String password);

    public void removeUserFromChannels(User user);

}
