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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/read-status")
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
      @PathVariable UUID userId
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
