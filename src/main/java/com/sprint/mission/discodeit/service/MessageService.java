package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.create.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Response.MessageResponse;
import com.sprint.mission.discodeit.dto.request.update.MessageUpdateRequest;

import java.util.*;

public interface MessageService {

    public MessageResponse create(MessageCreateRequest request, Optional<BinaryContentRequest> attachmentCreateRequest);

    public MessageResponse find(UUID id);

    public List<MessageResponse> findAllByChannelId(UUID channelId);

    public MessageResponse update(MessageUpdateRequest request);

    public boolean delete(UUID id);

}