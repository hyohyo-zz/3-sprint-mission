package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
@RequiredArgsConstructor
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  Instant updatedAt;
}
