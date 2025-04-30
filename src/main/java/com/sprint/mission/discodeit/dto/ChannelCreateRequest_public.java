package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ChannelCreateRequest_public(
        String channelName,
        UUID creatorId,
        List<String> categories,
        Set<UUID> memberIds
) {
}
