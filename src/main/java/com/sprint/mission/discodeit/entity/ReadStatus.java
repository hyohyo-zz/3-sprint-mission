package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/*사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현 하는 도메인
 * 사용자별 각 채널에 읽지 않은 메시지 확인하기
 * 메시지를 읽음 -> updatedAt 시간이 현재 시간으로*/
@Getter
@Setter
public class ReadStatus extends BaseUpdatableEntity {

  private User user;
  private Channel channel;
  private Instant lastReadAt;

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
