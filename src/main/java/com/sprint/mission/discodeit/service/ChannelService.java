package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_private;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_public;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest_public;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    public ChannelResponse createPublicChannel(ChannelCreateRequest_public request);

    public ChannelResponse createPrivateChannel(ChannelCreateRequest_private request);

    public ChannelResponse find(UUID id);

    public List<ChannelResponse> findAllByUserId(UUID id);

    public List<ChannelResponse> findByChannelName(String channelName);

    public ChannelResponse update(UUID channelId, ChannelUpdateRequest_public request);

    public boolean delete(UUID id, UUID userId, String password);

    public Set<User> members(UUID id);

}