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
    public Message createMessage(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message updateMessage(UUID id, Message updatedmessage) {
        if (!data.containsKey(id)) {
            return null;
        }
        updatedmessage.setUpdatedAt(System.currentTimeMillis());
        data.put(id, updatedmessage);
        return updatedmessage;
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


    public Message findById(UUID id) {
        return data.get(id);
    }

    public List<Message> Messages() {
        return new ArrayList<>(data.values());
    }

    public void getAllMessages(List<Channel> channels) {
        for (Channel channel : channels) {
            System.out.println("_________________________");
            System.out.println("[" + channel.getChannelName() + "] 채널 메시지 ▽");

            for (String category : channel.getCategory()) {
                System.out.println("- " + category);

                for (Message msg : getMessagesByChannel(channel.getChannelId())) {
                    if (msg.getCategory().equals(category)) {
                        System.out.println("  " + msg.getSender().getName() + ": " + msg.getContent());
                    }
                }
            }
        }
    }

}
