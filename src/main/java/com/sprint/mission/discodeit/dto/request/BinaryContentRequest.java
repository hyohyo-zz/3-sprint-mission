package com.sprint.mission.discodeit.dto.request;

public record BinaryContentRequest(
        byte[] content,
        String contentType,
        String originalFilename
) {
}
