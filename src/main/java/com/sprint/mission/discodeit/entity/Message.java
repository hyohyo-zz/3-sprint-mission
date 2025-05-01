package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID senderId;        //보낸사람
    private UUID channelId;
    private String category;
    private String content;     //내용
    private Instant createdAt;
    private Instant updatedAt;

    public Message(UUID senderId, UUID channelId, String category, String content) {
        this.id = UUID.randomUUID();
        this.senderId = senderId;
        this.channelId = channelId;
        this.category = category;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public void validateContent() {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 비어있을 수 없습니다.");
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Message message)) return false;
        return id.equals(message.id);
    }

    public String toString() {
        return "Message{" +
                "id= '" + id + '\'' +
                ", sender= '" + senderId + '\'' +
                ", channel= '" + channelId + '\'' +
                ", category= '" + category + '\'' +
                ", content= '" + content + '\'' +
                '}';
    }

}
