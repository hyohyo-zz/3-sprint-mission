package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * 메시지의 내용이 비어있어도 첨부 파일이 있다면 전송되어야 함
 */
public record MessageCreateRequest(
    String content,
    @NotBlank UUID channelId,
    UUID authorId
) {

}