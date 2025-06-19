package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    void deleteAllByChannelId(UUID channelId);

    @EntityGraph(attributePaths = {"author", "author.profile", "channel"})
    Slice<Message> findByChannelIdAndCreatedAtLessThan(UUID channelId, Instant cursor,
        Pageable pageable);

    @EntityGraph(attributePaths = {"author", "author.profile", "channel"})
    Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId);
}

