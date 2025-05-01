package com.sprint.mission.discodeit.dto.request.update;

import java.util.UUID;

public record MessageUpdateRequest(
        UUID messageId,
        String newContent
) {
}
