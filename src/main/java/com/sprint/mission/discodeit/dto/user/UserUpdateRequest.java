package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserUpdateRequest(
        @NotNull
        UUID id,
        String username,
        String email,
        String password,
        MultipartFile profileImage
) {
}
