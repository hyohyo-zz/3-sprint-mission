package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  public ReadStatus save(ReadStatus readStatus);

  public Optional<ReadStatus> findById(UUID id);

  public List<ReadStatus> findAllByUserId(UUID userId);

  public List<ReadStatus> findAllByChannelId(UUID channelId);

  public boolean existsById(UUID id);

  public void deleteById(UUID id);

  public void deleteAllByChannelId(UUID channelId);

  Optional<Object> findByUserIdAndChannelId(UUID userId, UUID channelId);
}
