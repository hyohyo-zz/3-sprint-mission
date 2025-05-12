package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.ErrorMessages;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID senderId;        //보낸사람
    private UUID channelId;
    private String category;
    private String content;     //내용

    private List<UUID> attachmentIds;

    private Instant createdAt;
    private Instant updatedAt;

    public Message(UUID senderId, UUID channelId, String category, String content, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.senderId = senderId;
        this.channelId = channelId;
        this.category = category;
        this.content = content;

        this.attachmentIds = new ArrayList<>();

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public void validateContent() {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("MessageContent", ErrorMessages.ERROR_EMPTY)
            );
        }
    }

    public void setAttachmentIds(List<UUID> attachmentIds) {
        this.attachmentIds = new ArrayList<>(attachmentIds);
        this.updatedAt = Instant.now();
    }

    public void addAttachmentId(UUID attachmentId) {
        if (attachmentIds == null) {
            this.attachmentIds = new ArrayList<>();
        }
        this.attachmentIds.add(attachmentId);
        this.updatedAt = Instant.now();
    }

}
