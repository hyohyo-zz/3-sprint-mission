package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/*사용자 별 마지막으로 확인된 접속시간 표현(사용자의 온라인 상태 확인)
* 마지막 접속 시간이 현재시간으로 부터 5분이내일때 접속중인 유저로 간주
* 접속중인 유저 = 현재 로그인한 유저로 판단
*
* 헷갈린다 updateAt을 마지막 접속시간으로 쓰면 안되는 것??
*
* updatedAt은 객체의 마지막 수정 시간(상태메시지, 로그인 등)
 * lastOnlineTime은 사용자가 언제 마지막으로 접속했는가??
* 나중에 메소드 추가를 위해? 분리하는 것이 좋다?*/
@Getter @Setter
public class UserStatus  implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userId;
    private Instant lastOnlineTime;

    private Instant createdAt;
    private Instant updatedAt;

    private boolean online;

    public UserStatus(UUID userId, Instant lastOnlineTime) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = Instant.now();
        this.lastOnlineTime = lastOnlineTime;
        this.online = isOnlineNow();
    }

    //lastOnlineTime update
    public void updatelastOnline(Instant lastOnlineTime) {
        boolean anyValueUpdated = false;
        if (lastOnlineTime != null && !lastOnlineTime.equals(this.lastOnlineTime)) {
            this.lastOnlineTime = lastOnlineTime;
            this.online = isOnlineNow();
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    //지금 온라인 상태인지?(5분 이내 인지)
    public boolean isOnlineNow() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        return lastOnlineTime.isAfter(instantFiveMinutesAgo);
    }
}
