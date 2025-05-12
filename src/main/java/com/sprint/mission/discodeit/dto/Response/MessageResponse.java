package com.sprint.mission.discodeit.dto.Response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID senderId,
        String content,
        Instant sentAt,
        List<UUID> attachmentIds
) {
}
