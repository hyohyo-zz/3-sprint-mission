package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User API", description = "사용자 관리")
public interface UserApi {

  @Operation(summary = "유저 생성", description = "새로운 유저를 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username을 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "text/plain"))
  })
  ResponseEntity<User> create(
      @Parameter(
          description = "User 생성 정보",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ) UserCreateRequest userCreateRequest,
      @Parameter(
          description = "User 프로필 이미지",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      ) MultipartFile profile
  );

  @Operation(summary = "전체 유저 조회", description = "전체 유저를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "유저 목록 조회 성공",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "array", implementation = User.class)))
  ResponseEntity<List<UserDto>> findAll();

  @Operation(summary = "유저 수정", description = "기존 유저 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "text/plain")),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "text/plain"))
  })
  ResponseEntity<User> update(
      @Parameter(description = "수정할 User ID") UUID userId,
      @Parameter(description = "수정할 User 정보") UserUpdateRequest userUpdateRequest,
      @Parameter(description = "수정할 User 프로필 이미지") MultipartFile profile
  );

  @Operation(summary = "유저 삭제", description = "기존 유저를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨",
          content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "text/plain"))
  })
  ResponseEntity<String> delete(
      @Parameter(description = "삭제할 User ID") UUID userId
  );

  @Operation(summary = "유저 상태 업데이트", description = "유저 상태를 업데이트합니다.(온라인)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(schema = @Schema(implementation = UserStatus.class))),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(mediaType = "text/plain")),
  })
  ResponseEntity<UserStatus> updateUserStatusByUserId(
      @Parameter(description = "상태를 변경할 User ID") UUID userId,
      @Parameter(description = "변경할 User 온라인 상태 정보") UserStatusUpdateRequest request
  );

}
