package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  public Message save(Message message);

  public Optional<Message> findById(UUID id);

  public List<Message> findAllByChannelId(UUID channelId);

  public boolean existsById(UUID id);

  public void deleteById(UUID id);

  public void deleteAllByChannelId(UUID channelId);

  public Slice<Message> findByChannelIdAndCreatedAtBeforeOrderByCreatedAtDesc(UUID channelId,
      Instant cursor, Pageable pageable);

  default Slice<Message> findByChannelIdAfter(UUID channelId, Instant cursor, Pageable pageable) {
    if (cursor == null) {
      return findByChannelIdOrderByCreatedAtDesc(channelId, pageable);
    }
    return findByChannelIdAndCreatedAtBeforeOrderByCreatedAtDesc(channelId, cursor, pageable);
  }

  Slice<Message> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);
}
