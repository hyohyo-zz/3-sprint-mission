package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
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
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @GetMapping
  public ResponseEntity<List<Message>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(messages);
  }

  @PatchMapping(path = "/{messageId}")
  public ResponseEntity<Message> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    Message updatedMessage = messageService.update(messageId, messageUpdateRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(updatedMessage);
  }

  @DeleteMapping(path = "/{messageId}")
  public ResponseEntity<String> delete(@PathVariable UUID messageId) {
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
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
          } catch (IOException e) {
            throw new RuntimeException("파일 읽기 중 오류 발생", e);
          }
        });
  }
}

