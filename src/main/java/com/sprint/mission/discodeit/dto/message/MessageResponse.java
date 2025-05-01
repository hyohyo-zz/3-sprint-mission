package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        String content,
        String senderName,
        Instant sentAt,
        List<BinaryContentResponse> attachments
) {
}
