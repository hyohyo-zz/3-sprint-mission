package com.sprint.mission.discodeit.dto.request.create;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId,
        Instant lastReadTime
) {
}
