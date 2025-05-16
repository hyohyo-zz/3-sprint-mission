package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.create.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatus> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    ReadStatus createStatus = readStatusService.create(readStatusCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createStatus);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @RequestParam("userId") UUID userId
  ) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatuses);
  }

  @PatchMapping(
      path = "/{readStatusId}/read-time"
//            , method = RequestMethod.PUT
  )
  public ResponseEntity<ReadStatus> update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest
  ) {
    ReadStatus updatedStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
    return ResponseEntity.ok(updatedStatus);
  }
}
