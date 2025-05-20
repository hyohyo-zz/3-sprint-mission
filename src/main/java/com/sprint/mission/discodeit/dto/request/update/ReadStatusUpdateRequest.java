package com.sprint.mission.discodeit.dto.request.update;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(
        Instant newReadTime
) {
}
