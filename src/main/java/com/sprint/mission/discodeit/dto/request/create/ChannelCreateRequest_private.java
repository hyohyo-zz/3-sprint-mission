package com.sprint.mission.discodeit.dto.request.create;

import java.util.List;
import java.util.UUID;

public record ChannelCreateRequest_private(
        List<UUID> memberIds
) {
}
