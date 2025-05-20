package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/*사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현 하는 도메인
* 사용자별 각 채널에 읽지 않은 메시지 확인하기
* 메시지를 읽음 -> updatedAt 시간이 현재 시간으로*/
@Getter @Setter
public class ReadStatus  implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadTime;

    private Instant createdAt;
    private Instant updatedAt;

    public ReadStatus(UUID userId, UUID channelId, Instant createdAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void update(Instant newLastReadTime) {
        boolean anyValueUpdated = false;
        if (newLastReadTime != null && !newLastReadTime.equals(this.lastReadTime)) {
            this.lastReadTime = newLastReadTime;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
