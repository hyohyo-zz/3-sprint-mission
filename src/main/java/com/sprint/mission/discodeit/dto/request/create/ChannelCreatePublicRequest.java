package com.sprint.mission.discodeit.dto.request.create;

import java.util.List;

public record ChannelCreatePublicRequest(
    String channelName,
    List<String> categories
) {

}
