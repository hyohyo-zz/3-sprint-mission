package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    public Message create(Message message);

    public Message find(UUID id);

    public List<Message> findAll();

    public boolean delete(UUID id);

    public Optional<Instant> findLastMessageTimeByChannelId(UUID channelId);

    public boolean deleteByChannelId(UUID channelId);
}
