package com.sprint.mission.discodeit.dto.request.update;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPhone,
        String newPassword
) {
}
