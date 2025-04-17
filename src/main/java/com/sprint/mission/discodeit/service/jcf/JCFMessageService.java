package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.time.LocalDateTime;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new LinkedHashMap<>();
    private final JCFUserService userService;
    private final JCFChannelService channelService;

    public JCFMessageService(JCFUserService userService, JCFChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    //메시지 생성
    @Override
    public void create(Message message) {
        Channel channel = message.getChannel();
        channel.validateMembership(message.getSender());
        channel.validateCategory(message.getCategory());

        message.validateContent();
        data.put(message.getId(), message);
    }

    //메시지 조회
    @Override
    public Message read(UUID id) {
        Message message = this.data.get(id);

        //메시지id 존재하지 않음
        if (message == null) {
            throw new IllegalArgumentException(" --해당 ID의 메시지를 찾을 수 없습니다.");
        }
        return this.data.get(id);
    }

    //메시지 전체조회
    @Override
    public List<Message> readAll() {
        return new ArrayList<>(this.data.values());
    }

    //메시지 수정
    @Override
    public Message update(UUID id, Message update) {
        Message selected = this.data.get(id);
        selected.update(update);
        return selected;
    }

    //메시지 삭제
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }


}
