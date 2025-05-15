package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

/*바이너리 데이터 표현 도메인
 * 프로필이미지, 메시지 첨부 파일 저장하기 위해
 * userId(messageId=null)-> 프로필이미지 반대면 메시지 첨부파일
 * 수정불가한 도메인
 * */
@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;

  private byte[] bytes;
  private String contentType;
  private String originalFilename;

  private Instant createdAt;

  public BinaryContent(byte[] bytes, String contentType, String originalFilename) {
    this.id = UUID.randomUUID();
    this.bytes = bytes;
    this.contentType = contentType;
    this.originalFilename = originalFilename;
    this.createdAt = Instant.now();
  }
}
