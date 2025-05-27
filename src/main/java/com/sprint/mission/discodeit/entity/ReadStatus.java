package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;

/*사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현 하는 도메인
 * 사용자별 각 채널에 읽지 않은 메시지 확인하기
 * 메시지를 읽음 -> updatedAt 시간이 현재 시간으로*/

@Entity
@Table(name = "read_statuses")
@Getter
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "channel_id", referencedColumnName = "id")
  private Channel channel;

  @Column(nullable = false)
  private Instant lastReadAt;

  public ReadStatus() {
  }

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void update(Instant newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
  }
}
