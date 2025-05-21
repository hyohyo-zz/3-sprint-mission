package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.UserStatusResponse;
import com.sprint.mission.discodeit.dto.request.create.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    public UserStatusResponse create(UserStatusCreateRequest request);

    public UserStatusResponse find(UUID id);

    public List<UserStatusResponse> findAll();

    public UserStatusResponse update(UserStatusUpdateRequest request);

    public UserStatusResponse updateByUserId(UUID userId, UserStatusUpdateRequest request);

    public boolean delete(UUID id);
}
