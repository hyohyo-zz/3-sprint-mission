package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
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

@Tag(name = "Message API", description = "메시지 관리")
public interface MessageApi {

  @Operation(summary = "메시지 생성", description = "메시지를 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(mediaType = "text/plain"))
  })
  ResponseEntity<Message> create(
      @Parameter(
          description = "Message 생성 정보",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ) MessageCreateRequest messageCreateRequest,
      @Parameter(
          description = "Message 첨부 파일들",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      ) List<MultipartFile> attachments
  );

  @Operation(summary = "채널 메시지 조회", description = "채널의 모든 메시지를 조회합니다.")
  @ApiResponse(
      responseCode = "200", description = "Message 목록 조회 성공",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "array", implementation = Message.class)))
  ResponseEntity<List<Message>> findAllByChannelId(
      @Parameter(description = "조회할 채널 ID") UUID channelId
  );

  @Operation(summary = "메시지 수정", description = "기존 메시지를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "text/plain")),
  })
  ResponseEntity<Message> update(
      @Parameter(description = "수정할 Message ID") UUID messageId,
      @Parameter(description = "Message 수정 정보") MessageUpdateRequest request
  );

  @Operation(summary = "메시지 삭제", description = "특정 메시지를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨",
          content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "text/plain"))
  })
  ResponseEntity<String> delete(
      @Parameter(description = "삭제할 Message ID") UUID messageId
  );

}
