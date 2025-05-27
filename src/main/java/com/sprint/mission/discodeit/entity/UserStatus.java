package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*사용자 별 마지막으로 확인된 접속시간 표현(사용자의 온라인 상태 확인)
 * 마지막 접속 시간이 현재시간으로 부터 5분이내일때 접속중인 유저로 간주
 * 접속중인 유저 = 현재 로그인한 유저로 판단
 *
 * 헷갈린다 updateAt을 마지막 접속시간으로 쓰면 안되는 것??
 *
 * updatedAt은 객체의 마지막 수정 시간(상태메시지, 로그인 등)
 * lastOnlineTime은 사용자가 언제 마지막으로 접속했는가??
 * 나중에 메소드 추가를 위해? 분리하는 것이 좋다?*/
@Entity
@Getter
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;

  public UserStatus() {
  }

  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  //lastActiveAt update
  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }

  //지금 온라인 상태인지?(5분 이내 인지)
  public Boolean isOnline() {
    Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

    return lastActiveAt.isAfter(instantFiveMinutesAgo);
  }
}
