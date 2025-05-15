package com.sprint.mission.discodeit.dto.request.update;

import java.util.List;

public record ChannelUpdateRequest_public(
    String newChannelName,
    List<String> newCategories
) {

}
