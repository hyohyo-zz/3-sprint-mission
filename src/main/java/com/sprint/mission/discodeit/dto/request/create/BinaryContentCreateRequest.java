package com.sprint.mission.discodeit.dto.request.create;

import java.util.UUID;

public record BinaryContentCreateRequest(
        UUID userId,
        UUID messageId,
        byte[] content,
        String contentType,
        String originalFilename
) {
}
