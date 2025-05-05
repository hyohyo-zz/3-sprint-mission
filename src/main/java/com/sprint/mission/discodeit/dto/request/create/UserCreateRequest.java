package com.sprint.mission.discodeit.dto.request.create;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserCreateRequest(
        String name,
        String email,
        String phone,
        String password,
        UUID profileImageId,    //선택, 없으면 null
        BinaryContentCreateRequest profileImage      //선택, 없으면 null
) {
}
