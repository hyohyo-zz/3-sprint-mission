package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  public Message create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests);

  public Message find(UUID id);

  public List<Message> findAllByChannelId(UUID channelId);

  public Message update(UUID messageId, MessageUpdateRequest request);

  public void delete(UUID id);

}