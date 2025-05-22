package com.sprint.mission.discodeit.dto.request.create;

import java.util.List;

public record ChannelCreateRequest_public(
        String channelName,
        List<String> categories
) {
}
