package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.UserDto;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    public UserDto create(UserCreateRequest request, Optional<BinaryContentCreateRequest> profileCreateRequest);

    public UserDto find(UUID id);

    public List<UserDto> findAll();

    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

    public void delete(UUID id, String password);
}
