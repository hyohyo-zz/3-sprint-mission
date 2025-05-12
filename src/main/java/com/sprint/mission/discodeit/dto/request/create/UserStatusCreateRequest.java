package com.sprint.mission.discodeit.dto.request.create;

import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId,
        boolean isOnline
) {
}
