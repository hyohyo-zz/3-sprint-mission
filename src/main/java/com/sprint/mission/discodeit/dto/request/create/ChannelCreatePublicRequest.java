package com.sprint.mission.discodeit.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ChannelCreatePublicRequest(
    @Schema(description = "생성할 채널 이름", example = "general")
    String channelName,

    @Schema(description = "채널에서 사용할 메시지 카테고리 목록", example = "[\"공지\", \"자유\", \"Q&A\"]")
    List<String> categories
) {

}
