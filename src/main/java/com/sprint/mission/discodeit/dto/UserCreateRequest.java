package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserCreateRequest(
        String name,
        String gender,
        String email,
        String phone,
        String password,
        UUID profileImageId,
        MultipartFile profileImage
) {
}
