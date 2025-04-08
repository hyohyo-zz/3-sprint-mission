package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;


import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void create(Channel channel) {
        this.data.put(channel.getId(),channel);
    }

    @Override
    public Channel read(UUID id) {
        return this.data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, Channel update) {
        Channel selected = this.data.get(id);
        selected.update(update);
        return selected;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public Set<User> members(UUID id) {
        Channel channel = data.get(id);
        return channel != null ? channel.getMembers() : Set.of();
    }

    @Override
    public List<String> cat(Channel channel) {
        return channel.getCategory();
    }

    //채널별 카테고리
    public Map<String, List<List<String>>> groupByChannel() {
        return data.values().stream()
                .collect(Collectors.groupingBy(
                        Channel::getChannelName,
                        Collectors.mapping(Channel::getCategory,
                                Collectors.toList())));
    }

    //특정 채널 정보
    public List<Channel> findChannel(String channelName) {
        return data.values().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }





}
