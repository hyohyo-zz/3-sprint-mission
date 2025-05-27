package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @Schema(description = "유저 이름", example = "조현아")
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    String username,

    @Schema(description = "유저 이메일", example = "hyeona@example.com")
    @NotBlank(message = "올바른 이메일 형식이 아닙니다.")
    String email,

    @Schema(description = "유저 비밀번호", example = "password123")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    String password
) {

}
