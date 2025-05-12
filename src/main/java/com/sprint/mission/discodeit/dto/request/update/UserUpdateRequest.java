package com.sprint.mission.discodeit.dto.request.update;

public record UserUpdateRequest(
        String newUserName,
        String newEmail,
        String newPhone,
        String newPassword
) {
}
