package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest_public;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  public ChannelResponse create(ChannelCreatePublicRequest request);

  public ChannelResponse create(ChannelCreatePrivateRequest request);

  public ChannelResponse find(UUID id);

  public List<ChannelResponse> findAllByUserId(UUID id);

  public ChannelResponse update(UUID channelId, ChannelUpdateRequest_public request);

  public void delete(UUID channelId);
}