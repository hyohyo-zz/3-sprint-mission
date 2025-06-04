package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  public void deleteAllByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {"author", "author.userStatus", "author.profile", "attachments"})
  public Slice<Message> findAllByChannelIdAndCreatedAtBefore(UUID channelId,
      Instant cursor, Pageable pageable);

  @EntityGraph(attributePaths = {"author", "author.userStatus", "author.profile", "attachments"})
  public Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
}
