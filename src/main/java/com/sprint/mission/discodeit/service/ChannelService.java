package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest_public;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  public ChannelDto create(ChannelCreatePublicRequest request);

  public ChannelDto create(ChannelCreatePrivateRequest request);

  public ChannelDto find(UUID id);

  public List<ChannelDto> findAllByUserId(UUID id);

  public ChannelDto update(UUID channelId, ChannelUpdateRequest_public request);

  public void delete(UUID channelId);
}