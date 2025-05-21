package com.sprint.mission.discodeit.dto.Response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID channelId,
        String channelName,
        List<String> categories,
        boolean isPrivate,
        Instant lastMessageTime,
        List<UUID> memberIds,
        UUID creatorId
) {
}
