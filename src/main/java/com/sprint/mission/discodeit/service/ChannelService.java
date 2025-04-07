package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.*;

public interface ChannelService {
    Channel createChannel(String ChannelName);
    Channel findById(UUID id);
    List<Channel> findAll();
    Channel update(UUID id, Channel updatedChannel);
    boolean delete(UUID id);

}