package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusDto> create(
        @RequestBody @Valid ReadStatusCreateRequest request) {
        ReadStatusDto createStatus = readStatusService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createStatus);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
        @RequestParam("userId") UUID userId) {
        List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(readStatuses);
    }

    @PatchMapping(path = "/{readStatusId}")
    public ResponseEntity<ReadStatusDto> update(
        @PathVariable("readStatusId") UUID readStatusId,
        @RequestBody ReadStatusUpdateRequest request
    ) {
        ReadStatusDto updatedStatus = readStatusService.update(readStatusId, request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedStatus);
    }
}
