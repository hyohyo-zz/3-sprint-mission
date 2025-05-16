package com.sprint.mission.discodeit.dto.request.create;

public record UserCreateRequest(
    String name,
    String email,
    String password
) {

}
