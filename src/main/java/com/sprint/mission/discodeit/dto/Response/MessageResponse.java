package com.sprint.mission.discodeit.dto.Response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID channelId,
        UUID senderId,
        String category,
        String content,
        Instant createAt,
        List<UUID> attachmentIds
) {
}
