package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus API", description = "메시지 수신 정보 관리")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "매시지 수신 정보 생성", description = "새로운 메시지 수신 정보를 생성합니다.")
  @PostMapping
  public ResponseEntity<ReadStatus> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    ReadStatus createStatus = readStatusService.create(readStatusCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createStatus);
  }

  @Operation(summary = "사용자 메시지 수신 정보 조회", description = "특정 사용자의 메시지 수신 정보를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @RequestParam("userId") UUID userId
  ) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatuses);
  }

  @Operation(summary = "메시지 수신 정보 수정", description = "특정 채널의 메시지 수신 정보를 수정합니다.")
  @PatchMapping(
      path = "/{readStatusId}"
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
