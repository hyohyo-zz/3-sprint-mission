package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;


import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel createChannel(Channel channel) {
        data.put(channel.getChannelId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> Channels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel updateChannel(UUID id, Channel updatedChannel) {
        if (!data.containsKey(id)) {
            return null;
        }
        Channel existing = data.get(id);
        existing.setChannelName(updatedChannel.getChannelName());
        existing.setCategory(updatedChannel.getCategory());
        existing.setMembers(updatedChannel.getMembers());
        existing.setUpdatedAt(System.currentTimeMillis());
        return existing;
    }

    @Override
    public boolean deleteChannel(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public Set<User> getChannelMembers(UUID id) {
        Channel channel = data.get(id);
        return channel != null ? channel.getMembers() : Set.of();
    }

    public Map<String, Long> countByChannel() {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getMajor, Collectors.counting()));
    }
}
