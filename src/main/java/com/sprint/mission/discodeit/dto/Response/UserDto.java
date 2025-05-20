package com.sprint.mission.discodeit.dto.Response;

import java.time.Instant;
import java.util.UUID;


public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
}
