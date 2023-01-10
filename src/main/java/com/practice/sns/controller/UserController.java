package com.practice.sns.controller;

import com.practice.sns.dto.UserDto;
import com.practice.sns.dto.request.UserJoinRequestDto;
import com.practice.sns.dto.request.UserLoginRequestDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.dto.response.UserJoinResponseDto;
import com.practice.sns.dto.response.UserLoginResponseDto;
import com.practice.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponseDto> join(@RequestBody UserJoinRequestDto dto) {
        UserDto userDto = userService.join(dto.getUserName(), dto.getPassword());
        return Response.success(UserJoinResponseDto.from(userDto));
    }

    @PostMapping("/login")
    public Response<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto dto) {
        String token = userService.login(dto.getUserName(), dto.getPassword());
        return Response.success(new UserLoginResponseDto(token));
    }
}
