package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();

    //채널 생성
    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
    }

    //채널 조회
    @Override
    public Channel find(UUID id) {
        return data.get(id);
    }

    //채널 전체 조회
    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    //특정 채널 정보
    public List<Channel> findByChannelName(String channelName) {
        return data.values().stream()
                .filter(channel -> channel.getChannelName().contains(channelName))
                .collect(Collectors.toList());
    }

    //채널 수정
    @Override
    public Channel update(UUID id, Channel update) {
        Channel channel = this.data.get(id);
        channel.update(update);
        return channel;
    }

    //채널 삭제
    @Override
    public boolean delete(UUID id, User user, String password) {
        return this.data.remove(id) != null;
    }

    //채널 멤버셋
    @Override
    public Set<User> members(UUID id) {
        Channel channel = data.get(id);
        return channel != null ? channel.getMembers() : Set.of();
    }

}
