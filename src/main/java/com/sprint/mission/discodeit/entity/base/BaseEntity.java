package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@RequiredArgsConstructor
public abstract class BaseEntity {

  private UUID id;

  @CreatedDate
  private Instant createdAt;

}
