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
    private final byte[] content;
    private final String contentType;
    private final String originalFilename;
    private final String url;
    private final Instant createdAt;

    public BinaryContent(byte[] content, String contentType, String originalFilename, String url, Instant createdAt) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.contentType = contentType;
        this.originalFilename = originalFilename;
        this.url = "/files/" + originalFilename;
        this.createdAt = createdAt;
    }
}
