package com.sprint.mission.discodeit.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateRequest(
    @Schema(description = "새로운 사용자 이름", example = "현아킹")
    String newUserName,

    @Schema(description = "새로운 이메일 주소", example = "hyuna_updated@example.com")
    String newEmail,

    @Schema(description = "새로운 전화번호", example = "010-1234-5678")
    String newPhone,

    @Schema(description = "새로운 비밀번호", example = "newSecurePassword123!")
    String newPassword

) {

}
