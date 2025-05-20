package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binarycontent")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(
            path = "/{binaryId}"
//            , method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<BinaryContentResponse> find(
            @PathVariable ("binaryId") UUID binaryId
    ) {
        BinaryContentResponse binaryContent = binaryContentService.find(binaryId);
        return ResponseEntity.ok(binaryContent);
    }

    @RequestMapping(
            path = "/findAll"
    )
    @ResponseBody
    public ResponseEntity<List<BinaryContentResponse>> findAll(
            @RequestParam("ids") List<UUID> ids
    ) {
        List<BinaryContentResponse> binaryContents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(binaryContents);
    }
}
