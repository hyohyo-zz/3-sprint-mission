package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Slice;

public interface MessageService {

  public Message create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests);

  public Message find(UUID id);

  public List<Message> findAllByChannelId(UUID channelId);

  public Message update(UUID messageId, MessageUpdateRequest request);

  public void delete(UUID id);

  //커서 기반 페이징 메서드
  public Slice<Message> findByChannelIdAfter(UUID channelId, Instant cursor, int size);

}