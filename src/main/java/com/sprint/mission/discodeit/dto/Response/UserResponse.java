package com.sprint.mission.discodeit.dto.Response;

import java.util.UUID;


public record UserResponse(
        UUID id,
        String userName,
        String email,
        String phone,
        String password,
        UUID profileImageId,
        String profileImageUrl,
        boolean online
) {
}
