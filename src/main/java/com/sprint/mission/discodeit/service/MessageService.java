package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;

import java.util.*;

public interface MessageService {

    public MessageResponse create(MessageCreateRequest request);

    public MessageResponse find(UUID id);

    public List<MessageResponse> findAllByChannelId(UUID channelId);

    public MessageResponse update(MessageUpdateRequest request);

    public boolean delete(UUID id);

}