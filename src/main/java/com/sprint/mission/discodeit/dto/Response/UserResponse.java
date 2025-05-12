package com.sprint.mission.discodeit.dto.Response;

import java.time.Instant;
import java.util.UUID;


public record UserResponse(
        UUID id,
        String userName,
        String email,
        String phone,
        String password,
        Instant createdAt,
        boolean online,
        UUID profileImageId,
        boolean hasProfileImage
) {
}
