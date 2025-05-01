package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

public record ChannelUpdateRequest(
        UUID channelId,
        String newChannelName,
        List<String> newCategories
) {
}
