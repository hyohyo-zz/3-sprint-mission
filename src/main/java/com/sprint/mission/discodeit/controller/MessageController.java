package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message API", description = "메시지 관리")
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메시지 생성", description = "메시지를 생성합니다.")
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {

    List<BinaryContentCreateRequest> attachmentRequests = new ArrayList<>();

    if (attachments != null) {
      for (MultipartFile file : attachments) {
        resolveAttachmentRequest(file).ifPresent(attachmentRequests::add);
      }
    }
    Message createdMessage = messageService.create(messageCreateRequest,
        attachmentRequests);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
  }

  @Operation(summary = "단일 메시지 조회", description = "특정 메시지를 조회합니다.")
  @GetMapping(
      path = "/{messageId}"
//            , method = RequestMethod.GET
  )
  public ResponseEntity<Message> find(
      @PathVariable UUID messageId
  ) {
    Message message = messageService.find(messageId);
    return ResponseEntity.ok(message);
  }

  @Operation(summary = "채널 메시지 조회", description = "채널의 모든 메시지를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<Message>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId
  ) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messages);
  }

  @Operation(summary = "메시지 수정", description = "기존 메시지를 수정합니다.")
  @PatchMapping(
      path = "/{messageId}"
      , consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Message> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    Message updatedMessage = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity.ok(updatedMessage);
  }

  @Operation(summary = "메시지 제거", description = "특정 메시지를 제거합니다.")
  @DeleteMapping(
      path = "/{messageId}"
//            , method = RequestMethod.DELETE
  )
  public ResponseEntity<String> delete(
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  private Optional<BinaryContentCreateRequest> resolveAttachmentRequest(
      MultipartFile attachment) {
    return Optional.ofNullable(attachment)
        .filter(file -> !file.isEmpty())
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
            );
          } catch (IOException e) {
            throw new RuntimeException("파일 읽기 중 오류 발생", e);
          }
        });
  }
}

