package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageRepository {
    @Override
    public void create(Message message) {

    }

    @Override
    public Message read(UUID id) {
        return null;
    }

    @Override
    public List<Message> readAll() {
        return List.of();
    }

    @Override
    public Message update(UUID id, Message update) {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }
}
