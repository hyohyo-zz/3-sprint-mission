package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  public Message save(Message message);

  public Optional<Message> findById(UUID id);

  public List<Message> findAllByChannelId(UUID channelId);

  public boolean existsById(UUID id);

  public void deleteById(UUID id);

  public void deleteAllByChannelId(UUID channelId);

  @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId AND m.createdAt > :cursor ORDER BY m.createdAt desc ")
  public List<Message> findAllByChannelIdAfterCursor(@Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor, Pageable pageable);

  List<Message> findByChannelId(UUID channelId, Pageable pageable);
}
