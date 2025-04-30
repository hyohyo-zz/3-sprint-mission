package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    public void create(Message message);

    public Message read(UUID id);

    public List<Message> readAll();

    public Message update(UUID id, Message update);

    public boolean delete(UUID id);

    public Optional<Instant> findLastMessageTimeByChannelId(UUID channelId);

    public boolean deleteByChannelId(UUID channelId);
}
