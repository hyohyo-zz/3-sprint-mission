package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public void create(Message message) {
        Channel channel = message.getChannel();
        channel.validateCategory(message.getCategory());
        channel.validateMembership(message.getSender());
        message.validateContent();
        messageRepository.create(message);
    }

    @Override
    public Message read(UUID id) {
        Message message = messageRepository.read(id);
        if (message == null) {
            throw new IllegalArgumentException(" --해당 메시지를 찾을 수 없습니다.");
        }
        return message;
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.readAll();
    }

    @Override
    public Message update(UUID id, Message update) {
        return messageRepository.update(id, update);
    }

    @Override
    public boolean delete(UUID id) {
        Message message = messageRepository.read(id);
        if (message == null) {
            throw new IllegalArgumentException(" --해당 메시지를 찾을 수 없습니다.");
        }
        return messageRepository.delete(id);
    }
}
