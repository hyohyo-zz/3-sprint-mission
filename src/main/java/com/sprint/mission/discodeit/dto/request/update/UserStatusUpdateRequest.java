package com.sprint.mission.discodeit.dto.request.update;

import java.util.UUID;

public record UserStatusUpdateRequest(
        UUID id,
        boolean newOnlineStatus
) {
}
