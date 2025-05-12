package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository{
    public Channel create(Channel channel);

    public Optional<Channel> find(UUID id);

    public List<Channel> findAll();

    public boolean delete(UUID id, UUID userId, String password);

    public Set<User> members(UUID id);

    public List<Channel> findByChannelName(String channelName);

}
