package com.practice.sns.controller;

import com.practice.sns.dto.UserDto;
import com.practice.sns.dto.response.NotificationResponseDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.service.NotificationService;
import com.practice.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/users/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    public Response<Page<NotificationResponseDto>> notificationList(Pageable pageable, Authentication authentication) {
        // DTO로 업캐스팅
        UserDto userDto = ClassUtils.getCastInstance(authentication.getPrincipal(), UserDto.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,
                        "Casting to UserDto class failed"));

        return Response.success(
                notificationService.getList(userDto.getId(), pageable).map(NotificationResponseDto::from));
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        UserDto userDto = ClassUtils.getCastInstance(authentication.getPrincipal(), UserDto.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,
                        "Casting to UserDto class failed"));
        return notificationService.connectNotification(userDto.getId());
    }
}
