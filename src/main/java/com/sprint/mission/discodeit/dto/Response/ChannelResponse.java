package com.sprint.mission.discodeit.dto.Response;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
    UUID channelId,
    ChannelType type,
    String channelName,
    List<String> categories,
    List<UUID> memberIds,
    Instant lastMessageAt
) {

}
