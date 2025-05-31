package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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
  private final MessageMapper messageMapper;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<MultipartFile> multipartAttachments = attachments != null ? attachments : List.of();

    List<BinaryContentCreateRequest> attachmentRequests = multipartAttachments.stream()
        .map(this::resolveAttachmentRequest)
        .flatMap(Optional::stream)
        .collect(Collectors.toList());

    Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
    MessageDto messageDto = messageMapper.toDto(createdMessage);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(messageDto);
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @RequestParam UUID channelId,
      @RequestParam(required = false) String cursor,
      @ParameterObject Pageable pageable
  ) {
    PageResponse<MessageDto> pageResponse = messageService.findByChannelIdWithCursor(channelId,
        cursor, pageable);
    return ResponseEntity.ok(pageResponse);
  }

  @PatchMapping(path = "/{messageId}")
  public ResponseEntity<MessageDto> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    Message updatedMessage = messageService.update(messageId, messageUpdateRequest);
    MessageDto messageDto = messageMapper.toDto(updatedMessage);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messageDto);
  }

  @DeleteMapping(path = "/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
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

