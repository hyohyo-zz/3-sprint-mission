package com.sprint.mission.discodeit.dto.request.create;

public record BinaryContentCreateRequest(
        byte[] bytes,
        String contentType,
        String originalFilename
) {
}
