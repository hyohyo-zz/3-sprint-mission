package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_private;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_public;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest_public;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(
            path = "/create_public"
//            , method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    public ResponseEntity<ChannelResponse> createPublicChannel(
            @RequestPart("channelCreateRequest_public") ChannelCreateRequest_public channelCreateRequest_public
    ) {
        ChannelResponse createdChannel = channelService.create(channelCreateRequest_public);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(
            path = "/create_private"
//            , method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    public ResponseEntity<ChannelResponse> createPrivateChannel(
            @RequestPart("channelCreateRequest_private") ChannelCreateRequest_private channelCreateRequest_private
    ) {
        ChannelResponse createdChannel_private = channelService.create(channelCreateRequest_private);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel_private);
    }

    @RequestMapping(
            path = "/{channelId}"
//            , method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<ChannelResponse> find(
            @PathVariable("channelId") UUID channelId
    ) {
        ChannelResponse channel = channelService.find(channelId);
        return ResponseEntity.ok(channel);
    }

    @RequestMapping(
            path = "/findAllByUserId"
//            , method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<List<ChannelResponse>> findAllByUserId(
            @RequestParam("userId") UUID userId
    ) {
        List<ChannelResponse> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }

    @RequestMapping(
            path = "/update"
//            , method = RequestMethod.PUT
    )
    @ResponseBody
    public ResponseEntity<ChannelResponse> update(
            @RequestParam("channelId") UUID channelId,
            @RequestPart("channelUpdateRequest_public") ChannelUpdateRequest_public channelUpdateRequest_public
    ) {
        ChannelResponse updatedChannel = channelService.update(channelId, channelUpdateRequest_public);
        return ResponseEntity.ok(updatedChannel);
    }

    @RequestMapping(
            path = "/delete"
//            , method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseEntity<String> delete(
            @RequestParam("channelId") UUID channelId
    ) {
        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.OK).body("채널 삭제 성공");
    }
}
