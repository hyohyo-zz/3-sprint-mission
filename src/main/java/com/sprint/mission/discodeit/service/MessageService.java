package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageService {

  public Message create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests);

  public Message find(UUID id);

  public List<Message> findAllByChannelId(UUID channelId);

  public Message update(UUID messageId, MessageUpdateRequest request);

  public void delete(UUID id);

  //커서 기반 페이징 메서드
  public PageResponse<MessageDto> findByChannelIdWithCursor(UUID channelId, String cursor,
      Pageable pageable);

}