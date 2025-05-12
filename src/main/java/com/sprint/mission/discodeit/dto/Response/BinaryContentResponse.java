package com.sprint.mission.discodeit.dto.Response;

import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String contentType,
        String originalFilename
) {
}
