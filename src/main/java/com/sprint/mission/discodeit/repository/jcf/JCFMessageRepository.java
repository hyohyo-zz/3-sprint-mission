package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.time.Instant;
import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new LinkedHashMap<>();

    //메시지 생성
    @Override
    public Message create(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    //메시지 조회
    @Override
    public Message find(UUID id) {
        return this.data.get(id);
    }

    //메시지 전체조회
    @Override
    public List<Message> findAll() {
        return new ArrayList<>(this.data.values());
    }

    //메시지 삭제
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public Optional<Instant> findLastMessageTimeByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(msg -> Objects.equals(msg.getChannelId(), channelId))
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder());
    }

    @Override
    public boolean deleteByChannelId(UUID channelId) {
        List<UUID> toRemove = data.values().stream()
                .filter(message -> Objects.equals(message.getChannelId(), channelId))
                .map(Message::getId)
                .toList();

        boolean deleted = false;
        for (UUID id : toRemove) {
            deleted |= data.remove(id) != null;
        }

        return deleted;
    }
}
