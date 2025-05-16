package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

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