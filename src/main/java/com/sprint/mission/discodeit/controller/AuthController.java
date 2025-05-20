package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @RequestMapping(
            path = "/login"
            ,method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<UserResponse> login(
            @RequestPart("loginRequest") LoginRequest loginRequest
    ) {

        User user = authService.login(loginRequest);
        UserResponse userResponse = userService.find(user.getId());

        return ResponseEntity.ok(userResponse);
    }
}