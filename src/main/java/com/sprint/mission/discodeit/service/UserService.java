package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.*;

public interface UserService {
    User createUser(User user);
    User findById(UUID id);
    List<User> Users();
    User updateUser(UUID id, User updatedUser);
    boolean deleteUser(UUID id);


}
