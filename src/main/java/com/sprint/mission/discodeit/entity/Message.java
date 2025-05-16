package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.ErrorMessages;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;

  private String content;     //내용
  private UUID senderId;        //보낸사람
  private UUID channelId;
  private String category;

  private List<UUID> attachmentIds;


  public Message(UUID channelId, UUID senderId, String category, String content,
      List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.channelId = channelId;
    this.senderId = senderId;
    this.category = category;
    this.content = content;

    this.attachmentIds = attachmentIds;

    this.createdAt = Instant.now();
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public void validateContent() {
    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException(
          ErrorMessages.format("MessageContent", ErrorMessages.ERROR_EMPTY)
      );
    }
  }
}
