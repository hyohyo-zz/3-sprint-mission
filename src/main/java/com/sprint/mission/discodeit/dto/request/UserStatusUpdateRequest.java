package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotBlank
    Instant newLastActiveAt
) {

}
