package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;

/*사용자 별 마지막으로 확인된 접속시간 표현(사용자의 온라인 상태 확인)
 * 마지막 접속 시간이 현재시간으로 부터 5분이내일때 접속중인 유저로 간주
 * 접속중인 유저 = 현재 로그인한 유저로 판단
 */
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
