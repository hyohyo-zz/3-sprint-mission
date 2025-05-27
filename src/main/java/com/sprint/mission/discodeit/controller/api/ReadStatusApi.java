package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "ReadStatus API", description = "메시지 수신 정보 관리")
public interface ReadStatusApi {

  @Operation(summary = "매시지 수신 정보 생성", description = "새로운 메시지 수신 정보를 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(mediaType = "text/plain")),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함",
          content = @Content(mediaType = "text/plain"))
  })
  ResponseEntity<ReadStatus> create(
      @Parameter(description = "Message 읽음 상태 생성 정보") ReadStatusCreateRequest request
  );

  @Operation(summary = "사용자 메시지 수신 정보 조회", description = "특정 사용자의 메시지 수신 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "array", implementation = ReadStatus.class)))
  ResponseEntity<List<ReadStatus>> findAllByUserId(
      @Parameter(description = "조회할 User ID") UUID userId
  );

  @Operation(summary = "메시지 수신 정보 수정", description = "특정 채널의 메시지 수신 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(mediaType = "text/plain")),
  })
  ResponseEntity<ReadStatus> update(
      @Parameter(description = "수정할 읽음 상태 ID") UUID readStatusId,
      @Parameter(description = "수정할 읽음 상태 정보") ReadStatusUpdateRequest request
  );
}


