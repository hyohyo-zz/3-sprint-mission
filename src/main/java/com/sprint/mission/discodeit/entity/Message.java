package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends BaseUpdatableEntity {

  private String content;     //내용
  private UUID channelId;
  private User author;        //보낸사람
  private List<BinaryContent> attachmentIds;

  public Message(String content, UUID channelId, User author, List<BinaryContent> attachmentIds) {
    this.content = content;
    this.channelId = channelId;
    this.author = author;
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
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
