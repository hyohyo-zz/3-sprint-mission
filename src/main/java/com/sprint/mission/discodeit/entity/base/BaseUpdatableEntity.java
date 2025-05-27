package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@RequiredArgsConstructor
public abstract class BaseUpdatableEntity {

  @LastModifiedDate
  Instant updatedAt;
}
