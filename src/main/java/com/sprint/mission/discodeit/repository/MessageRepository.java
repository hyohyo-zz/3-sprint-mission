package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  public void deleteAllByChannelId(UUID channelId);

  @Query("""
      SELECT m FROM Message m 
      LEFT JOIN FETCH m.attachments 
      WHERE m.channel.id = :channelId 
      AND m.createdAt < :cursor 
      ORDER BY m.createdAt DESC 
      """)
  public Slice<Message> findAllByChannelIdAfterCursor(@Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor, Pageable pageable);

  public Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
}
