package com.sprint.mission.discodeit.dto.request.create;

import java.util.UUID;

public record MessageCreateRequest(
    UUID senderId,
    UUID channelId,
    String category,
    String content
) {

}
