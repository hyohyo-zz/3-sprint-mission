package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;
  private final MessageService messageService;
  private final PageResponseMapper pageResponseMapper;
  private final MessageMapper messageMapper;
  private final ChannelMapper channelMapper;

  @PostMapping(path = "/public")
  public ResponseEntity<ChannelDto> create(@RequestBody PublicChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    ChannelDto channelDto = channelMapper.toDto(createdChannel);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channelDto);
  }

  @PostMapping(path = "/private")
  public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    ChannelDto channelDto = channelMapper.toDto(createdChannel);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channelDto);
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }

  @PatchMapping(path = "/{channelId}")
  public ResponseEntity<ChannelDto> update(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel updatedChannel = channelService.update(channelId, request);
    ChannelDto channelDto = channelMapper.toDto(updatedChannel);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channelDto);
  }

  @DeleteMapping(path = "/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);

    return ResponseEntity.noContent().build();
  }
}
