package com.sprint.mission.discodeit.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserCreateRequest(

    @Schema(description = "유저 이름", example = "현아공주")
    String name,

    @Schema(description = "유저 이메일", example = "hyuna@example.com")
    String email,

    @Schema(description = "유저 비밀번호", example = "securePassword123!")
    String password
) {

}
