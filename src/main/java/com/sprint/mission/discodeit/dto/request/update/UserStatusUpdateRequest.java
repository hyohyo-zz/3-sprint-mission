package com.sprint.mission.discodeit.dto.request.update;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastOnlineTime
) {

}
