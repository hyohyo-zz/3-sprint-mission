package com.sprint.mission.discodeit.dto.binarycontent;

public record BinaryContentRequest(
        byte[] content,
        String contentType,
        String originalFilename
) {
}
