package com.sprint.mission.discodeit.dto.request.create;

import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        UUID senderId,
        UUID channelId,
        String category,
        String content,
        List<BinaryContentRequest> attachments
) {
}
