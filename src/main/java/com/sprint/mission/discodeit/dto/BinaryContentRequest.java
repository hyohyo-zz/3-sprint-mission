package com.sprint.mission.discodeit.dto;

public record BinaryContentRequest(
        byte[] content,
        String contentType,
        String originalFilename
) {
}
