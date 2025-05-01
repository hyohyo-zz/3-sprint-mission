package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest_private;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest_public;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public interface ChannelService {
    public ChannelResponse createPublicChannel(ChannelCreateRequest_public channel);

    public ChannelResponse createPrivateChannel(ChannelCreateRequest_private channel);

    public ChannelResponse find(UUID id);

    public List<ChannelResponse> findAllByUserId();

    public List<ChannelResponse> findByChannelName(String channelName);

    public ChannelResponse update(ChannelUpdateRequest request);

    public boolean delete(UUID id, User user, String password);

    public Set<User> members(UUID id);

    public List<ChannelResponse> findAllByUserId(UUID id);

}