package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserCreateRequest(
        UUID id,
        String name,
        String email,
        UUID profileId
) {
}
