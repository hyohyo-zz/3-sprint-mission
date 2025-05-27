package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  @Column
  private String content;     //내용

  @ManyToOne
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  private User author;        //보낸사람

  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachmentIds;

  public Message() {
  }

  public Message(String content, Channel channel, User author, List<BinaryContent> attachmentIds) {
    this.content = content;
    this.channel = channel;
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
