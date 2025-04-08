package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.time.LocalDateTime;
import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void create(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Message update(UUID id, Message update) {
        Message selected = this.data.get(id);
        selected.update(update);
        return selected;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public Message read(UUID id) {
        return this.data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(this.data.values());
    }


}
