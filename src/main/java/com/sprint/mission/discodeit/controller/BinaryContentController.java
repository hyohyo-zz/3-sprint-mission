package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @GetMapping("{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContentDto);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    List<BinaryContentDto> binaryContentDtos = binaryContents.stream()
        .map(binaryContentMapper::toDto)
        .toList();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContentDtos);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    BinaryContentDto binaryContentDto = new BinaryContentDto(
        binaryContent.getId(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType()
    );
    return binaryContentStorage.download(binaryContentDto);
  }
}
