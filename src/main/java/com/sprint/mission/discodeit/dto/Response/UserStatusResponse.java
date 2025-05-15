package com.sprint.mission.discodeit.dto.Response;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
    UUID userId,
    boolean online,
    Instant lastOnlineTime
) {

}
