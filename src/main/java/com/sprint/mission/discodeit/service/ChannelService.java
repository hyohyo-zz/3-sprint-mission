package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public interface ChannelService {
    Channel createChannel(Channel channel);
    Channel findById(UUID id);
    List<Channel> Channels();
    Channel updateChannel(UUID id, Channel updatedChannel);
    boolean deleteChannel(UUID id);

    Set<User> getChannelMembers(UUID id);
}