package com.practice.sns.controller;

import com.practice.sns.dto.response.NotificationResponseDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    public Response<Page<NotificationResponseDto>> notificationList(Pageable pageable, Authentication authentication) {
        return Response.success(
                notificationService.getList(authentication.getName(), pageable).map(NotificationResponseDto::from));
    }
}
