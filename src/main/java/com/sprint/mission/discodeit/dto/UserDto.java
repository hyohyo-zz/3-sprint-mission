package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String name,
        String email,
        String gender,
        String phone,
        UUID profileImageId,
        boolean online
) {
}
