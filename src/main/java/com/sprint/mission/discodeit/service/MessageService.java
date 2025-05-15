package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.MessageResponse;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  public MessageResponse create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests);

  public MessageResponse find(UUID id);

  public List<MessageResponse> findAllByChannelId(UUID channelId);

  public MessageResponse update(UUID messageId, MessageUpdateRequest request);

  public void delete(UUID id);

}