package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

/**
 * 메시지의 내용이 비어있어도 첨부 파일이 있다면 전송되어야 함
 */
public record MessageCreateRequest(

    String content,
    UUID channelId,
    UUID authorId
) {

}