package com.sprint.mission.discodeit.dto;

import java.util.UUID;


public record UserResponse(
        UUID id,
        String userName,
        String email,
        String profileImageUrl,
        boolean online
) {
}
