package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public void create(Channel channel) {
        validateDuplicateChannelName(channel);
        channel.validateUniqueCategory();
        channel.addKeyUserToMembers();
        channelRepository.create(channel);
    }

    @Override
    public Channel read(UUID id) {
        Channel channel = channelRepository.read(id);
        if (channel == null) {
            throw new IllegalArgumentException(" --해당 ID의 채널을 찾을 수 없습니다.");
        }
        return channel;
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.readAll();
    }

    @Override
    public List<Channel> readByName(String channelName) {
        return channelRepository.readByName(channelName);
    }

    @Override
    public Channel update(UUID id, Channel update) {
        Channel channel = channelRepository.read(id);
        if (channel == null) {
            throw new IllegalArgumentException(" --해당 ID의 채널을 찾을 수 없습니다.");
        }
        channel.update(update);
        return channel;
    }

    @Override
    public boolean delete(UUID id, User user, String password) {
        Channel channel = channelRepository.read(id);
        if (channel == null) {
            throw new IllegalArgumentException(" --해당 채널을 찾을 수 없습니다.");
        }
        return channelRepository.delete(id, user, password);
    }

    @Override
    public Set<User> members(UUID id) {
        return channelRepository.members(id);
    }

    private void validateDuplicateChannelName(Channel channel) {
        List<Channel> channels = channelRepository.readByName(channel.getChannelName());
        if (channels.stream().anyMatch(c -> c.getKeyUser().equals(channel.getKeyUser())
                && c.getChannelName().equals(channel.getChannelName()))) {
            throw new IllegalArgumentException(" --- 이미 등록된 채널입니다.");
        }
    }
}
