package com.sprint.mission.discodeit.dto.data;

public record JwtDto(
    UserDto user,
    String accessToken
) {

}
