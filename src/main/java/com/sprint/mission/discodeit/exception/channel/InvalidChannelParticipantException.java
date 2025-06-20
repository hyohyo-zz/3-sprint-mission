package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class InvalidChannelParticipantException extends ChannelException {

    public InvalidChannelParticipantException(UUID channelId, Set<UUID> invalidUserIds) {
        super(ErrorCode.INVALID_CHANNEL_PARTICIPANT,
            Map.of("channelId", channelId, "invalidUserIds", invalidUserIds));
    }
}
