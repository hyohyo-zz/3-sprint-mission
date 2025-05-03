package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/*바이너리 데이터 표현 도메인
* 프로필이미지, 메시지 첨부 파일 저장하기 위해
* userId(messageId=null)-> 프로필이미지 반대면 메시지 첨부파일
* 수정불가한 도메인
* */
@Getter
public class BinaryContent  implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;

    private final UUID userId;      //프로필이미지
    private final UUID messageId;   //첨부파일

    private final byte[] content;
    private final String contentType;
    private final String originalFilename;
    private final String url;
    private final Instant createdAt;

    public BinaryContent(UUID userId, UUID messageId, byte[] content, String contentType, String originalFilename) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.messageId = messageId;
        this.content = content;
        this.contentType = contentType;
        this.originalFilename = originalFilename;
        this.url = "/files/" + originalFilename;
        this.createdAt = Instant.now();
    }
}
