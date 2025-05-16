package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {

    Optional<BinaryContentCreateRequest> profileRequest =
        Optional.ofNullable(profile)
            .flatMap(this::resolveProfileRequest);

    UserDto createdUser = userService.create(userCreateRequest, profileRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @GetMapping(
      path = "/{userId}"
//            , method = RequestMethod.GET
  )
  public ResponseEntity<UserDto> find(
      @PathVariable UUID userId
  ) {
    UserDto user = userService.find(userId);
    return ResponseEntity.ok(user);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @PatchMapping(
      path = "/{userId}"
//            , method = RequestMethod.PUT
      , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest =
        Optional.ofNullable(profile)
            .flatMap(this::resolveProfileRequest);

    UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping(
      value = "/{userId}"
//            , method = RequestMethod.DELETE
  )
  public ResponseEntity<String> delete(
      @PathVariable UUID userId
  ) {
    userService.delete(userId);
    return ResponseEntity.noContent().build(); // 204
  }

  @PatchMapping(
      value = "/{userId}/userStatus"
//            , method = RequestMethod.PUT
  )
  public ResponseEntity<String> updateStatus(
      @PathVariable UUID userId
  ) {
    userStatusService.updateByUserId(userId, new UserStatusUpdateRequest(Instant.now()));
    return ResponseEntity.ok("userStatus update 성공");
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profile) {
    return Optional.ofNullable(profile)
        .filter(file -> !file.isEmpty())
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
            );
          } catch (IOException e) {
            throw new RuntimeException("프로필 업로드 중 오류 발생", e);
          }
        });
  }
}
