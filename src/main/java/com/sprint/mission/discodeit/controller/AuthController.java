package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "권한 관리")
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @Operation(summary = "유저 로그인", description = "사용자가 로그인합니다.")
  @RequestMapping(
      path = "/login"
      , method = RequestMethod.POST
  )
  @ResponseBody
  public ResponseEntity<UserDto> login(
      @RequestBody LoginRequest loginRequest
  ) {
    User user = authService.login(loginRequest);
    UserDto userDto = userService.find(user.getId());

    return ResponseEntity.ok(userDto);
  }
}