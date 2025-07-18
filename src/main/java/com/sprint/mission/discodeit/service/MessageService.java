package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    MessageDto create(MessageCreateRequest request,
        List<BinaryContentCreateRequest> attachmentRequests);

    MessageDto find(UUID id);

    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
        Pageable pageable);

    MessageDto update(UUID messageId, MessageUpdateRequest request);

    void delete(UUID id);

}