package com.sprint.mission.discodeit.dto.request.create;

import java.time.Instant;

public record UserStatusCreateRequest(
        Instant newLastOnlineTime
) {
}
