package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message createMessage(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message updateMessage(UUID id, Message updatedmessage) {
        if (!data.containsKey(id)) {
            return null;
        }
        return data.get(id);

    }

    @Override
    public boolean deleteMessage(UUID id) {
        return data.remove(id) != null;
    }

    public List<Message> getMessagesByChannel(UUID channelId) {
        List<Message> messages = new ArrayList<>();
        for (Message message : data.values()) {
            if (message.getChannel().getChannelId().equals(channelId)) {
                messages.add(message);
            }
        }
        return messages;
    }



}
