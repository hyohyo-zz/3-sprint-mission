package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    public BinaryContentResponse create(BinaryContentCreateRequest request);

    public BinaryContentResponse find(UUID id);

    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids);

    public boolean delete(UUID id);

}
