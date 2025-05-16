package com.sprint.mission.discodeit.dto.request.create;

import java.util.UUID;

public record MessageCreateRequest(
    UUID channelId,
    UUID senderId,
    String category,
    String content
) {

}
