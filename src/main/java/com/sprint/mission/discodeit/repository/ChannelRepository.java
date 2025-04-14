package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository{
    public void create(Channel channel);

    public Channel read(UUID id);

    public List<Channel> readAll();

    public Channel update(UUID id, Channel update);

    public boolean delete(UUID id, User user, String password);

    public Set<User> members(UUID id);

    public List<Channel> readByName(String channelName);

    public Map<String, List<List<String>>> groupByChannel();

}
