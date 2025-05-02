package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.dto.request.update.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.*;

public interface UserService {

    public UserResponse create(UserCreateRequest request);

    public UserResponse find(UUID id);

    public UserResponse findByUserName(String name);

    public List<UserResponse> findAll();

    public UserResponse update(UserUpdateRequest request);

    public boolean delete(UUID id, String password);

    public void removeUserFromChannels(User user);

}
