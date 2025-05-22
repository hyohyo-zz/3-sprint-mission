package com.sprint.mission.discodeit.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record MessageCreateRequest(

    @Schema(description = "메시지를 보낼 채널 ID", example = "d28d7d8e-25e5-4a3f-9ebf-ffb3a73d541e")
    UUID channelId,

    @Schema(description = "메시지를 보내는 유저 ID", example = "4a92e21c-fcd5-40ce-883a-6cc0950a9a7c")
    UUID senderId,

    @Schema(description = "메시지 카테고리 (예: 공지, 일반)", example = "공지")
    String category,

    @Schema(description = "메시지 본문 내용", example = "오늘 회의는 오후 3시에 시작합니다.")
    String content
) {

}
