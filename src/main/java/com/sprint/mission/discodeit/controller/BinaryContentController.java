package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContent")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(
            path = "/find"
//            , method = RequestMethod.GET
    )
    public ResponseEntity<BinaryContent> find(
            @RequestParam ("binaryContentId") UUID binaryId
    ) {
        BinaryContent binaryContent = binaryContentService.find(binaryId);
        return ResponseEntity.ok(binaryContent);
    }

    @RequestMapping(
            path = "/findAll"
    )
    @ResponseBody
    public ResponseEntity<List<BinaryContent>> findAll(
            @RequestParam("ids") List<UUID> ids
    ) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(binaryContents);
    }
}
