package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class DuplicateReadStatusException extends ReadStatusException {

    public DuplicateReadStatusException(UUID userId, UUID channelId) {
        super(ErrorCode.READSTATUS_ALREADY_EXISTS,
            Map.of("userId", userId, "channelId", channelId));
    }

}
