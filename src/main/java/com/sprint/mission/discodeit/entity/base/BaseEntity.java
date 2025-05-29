package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
@Getter
@RequiredArgsConstructor
public abstract class BaseEntity {

  @Id
  private UUID id;

  @CreatedDate
  @Column(nullable = false)
  private Instant createdAt;

}
