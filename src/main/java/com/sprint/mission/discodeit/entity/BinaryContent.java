package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/*바이너리 데이터 표현 도메인
* 프로필이미지, 메시지 첨부 파일 저장하기 위해
* 수정불가한 도메인
* */
@Getter
public class BinaryContent {
    private final UUID id;
    private final UUID userId;
    private final UUID messageId;

    private final byte[] content;
    private final String contentType;

    private final Instant createdAt;

    public BinaryContent(UUID userId, UUID messageId, byte[] content, String contentType, Instant createdAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.messageId = messageId;
        this.content = content;
        this.contentType = contentType;
        this.createdAt = Instant.now();
    }
}
