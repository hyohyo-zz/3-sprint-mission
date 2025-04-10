package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import jdk.jfr.Category;

import java.util.*;

public interface ChannelService {
    public void create(Channel channel);

    public Channel read(UUID id);

    public List<Channel> readAll();

    public Channel update(UUID id, Channel update);

    public boolean delete(UUID id, User user, String password);

    public Set<User> members(UUID id);

}