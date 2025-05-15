package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest_public;
import com.sprint.mission.discodeit.service.ChannelService;
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
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping(
      path = "/public"
  )
  public ResponseEntity<ChannelResponse> createPublicChannel(
      @RequestBody ChannelCreatePublicRequest channelCreatePublicRequest
  ) {
    ChannelResponse createdChannel = channelService.create(channelCreatePublicRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
  }

  @PostMapping(
      path = "/private"
  )
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody ChannelCreatePrivateRequest channelCreatePrivateRequest
  ) {
    ChannelResponse createdChannel_private = channelService.create(channelCreatePrivateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel_private);
  }

  @GetMapping(
      path = "/{channelId}"
      //            , method = RequestMethod.GET
  )
  public ResponseEntity<ChannelResponse> find(
      @PathVariable UUID channelId
  ) {
    ChannelResponse channel = channelService.find(channelId);
    return ResponseEntity.ok(channel);
  }

  @GetMapping
  public ResponseEntity<List<ChannelResponse>> findAllByUserId(
      @RequestParam("userId") UUID userId
  ) {
    List<ChannelResponse> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }

  @PatchMapping(
      path = "/{channelId}"
      //            , method = RequestMethod.PUT
  )
  public ResponseEntity<ChannelResponse> update(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest_public channelUpdateRequest_public
  ) {
    ChannelResponse updatedChannel = channelService.update(channelId,
        channelUpdateRequest_public);
    return ResponseEntity.ok(updatedChannel);
  }

  @DeleteMapping(
      path = "/{channelId}"
      //            , method = RequestMethod.DELETE
  )
  public ResponseEntity<String> delete(
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}
