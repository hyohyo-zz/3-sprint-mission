package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(
            path = "/create"
//            , method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    public ResponseEntity<UserResponse> create(
            @RequestParam("userCreateRequest") UserCreateRequest userCreateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile
            ) {

        Optional<BinaryContentRequest> profileRequest =
                Optional.ofNullable(profile)
                        .flatMap(this::resolveProfileRequest);

        UserResponse createdUser = userService.create(userCreateRequest, profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<UserResponse> find(
            @PathVariable("userId") UUID userId
    ) {
        UserResponse user = userService.find(userId);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(
            value = "/update",
            method = RequestMethod.PUT
    )
    @ResponseBody
    public ResponseEntity<UserResponse> update(
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        UserResponse updatedUser = userService.update(userUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(
            value = "/delete",
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseEntity<String> delete(
            @RequestParam("userId") UUID userId,
            @RequestParam("password") String password
    ) {
        boolean deleted = userService.delete(userId, password);
        if(deleted) {
            return ResponseEntity.ok("유저 삭제 성공");
        } else {
            return ResponseEntity.badRequest().body("password" + ErrorMessages.ERROR_MISMATCH);
        }
    }

    private Optional<BinaryContentRequest> resolveProfileRequest(MultipartFile profile) {

        if(profile.isEmpty()) {
            //컨트롤러가 요청받은 파라미터 중 MultipartFile 타입의 데이터가 비어있다면:
            return Optional.empty();
        } else {
            //컨트롤러가 요청받은 파라미터 중 MultipartFile 타입의 데이터가 존재한다면:
            try{
                BinaryContentRequest binaryContentRequest = new BinaryContentRequest(
                        profile.getBytes(),
                        profile.getContentType(),
                        profile.getOriginalFilename()
                );
                return Optional.of(binaryContentRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
