package com.sprint.mission.discodeit.dto.request.update;

import java.util.List;
import java.util.UUID;

public record ChannelUpdateRequest_public(
        String newChannelName,
        List<String> newCategories
) {
}
