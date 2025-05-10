package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.Response.MessageResponse;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_private;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_public;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest;
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
@RequestMapping("/api/channel")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(
            path = "/create_public"
//            , method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    public ResponseEntity<ChannelResponse> createPublicChannel(
            @RequestParam("channelCreateRequest_public") ChannelCreateRequest_public channelCreateRequest_public
    ) {
        ChannelResponse createdChannel = channelService.createPublicChannel(channelCreateRequest_public);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(
            path = "/create_private"
//            , method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    public ResponseEntity<ChannelResponse> createPrivateChannel(
            @RequestParam("channelCreateRequest_private") ChannelCreateRequest_private channelCreateRequest_private
    ) {
        ChannelResponse createdChannel_private = channelService.createPrivateChannel(channelCreateRequest_private);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel_private);
    }

    @RequestMapping(
            value = "/{channelId}",
            method = RequestMethod.GET
    )

    @ResponseBody
    public ResponseEntity<ChannelResponse> find(
            @PathVariable("channelId") UUID channelId
    ) {
        ChannelResponse channel = channelService.find(channelId);
        return ResponseEntity.ok(channel);
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<List<ChannelResponse>> findAllByUserId(
            @RequestParam("channelId") UUID userId
    ) {
        List<ChannelResponse> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }

    @RequestMapping(
            value = "/update",
            method = RequestMethod.PUT
    )
    @ResponseBody
    public ResponseEntity<ChannelResponse> update(
            @RequestBody ChannelUpdateRequest channelUpdateRequest
    ) {
        MessageResponse updatedChannel = channelService.update(channelUpdateRequest);
        return ResponseEntity.ok(updatedChannel);
    }

    @RequestMapping(
            value = "/delete",
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseEntity<String> delete(
            @RequestParam("channelId") UUID channelId,
            @RequestParam("userId") UUID creatorId,
            @RequestParam("password") String creatorPassword
    ) {
        boolean deleted = channelService.delete(channelId, creatorId, creatorPassword);
        if(deleted) {
            return ResponseEntity.ok("유저 삭제 성공");
        } else {
            return ResponseEntity.badRequest().body("삭제 실패!");
        }
    }
}
