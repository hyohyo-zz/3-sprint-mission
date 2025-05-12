package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    public UserResponse create(UserCreateRequest request, Optional<BinaryContentCreateRequest> profileCreateRequest);

    public UserResponse find(UUID id);

    public UserResponse findByUserName(String name);

    public List<UserResponse> findAll();

    public UserResponse update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

    public boolean delete(UUID id, String password);

//    public void removeUserFromChannels(User user);

    public List<UserResponse> findByUserNameKeyWords(String keyword);

}
