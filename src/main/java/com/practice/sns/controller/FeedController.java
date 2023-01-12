package com.practice.sns.controller;

import com.practice.sns.dto.response.PostResponseDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping()
    public Response<Page<PostResponseDto>> list(Pageable pageable, Authentication authentication) {
        return Response.success(feedService.list(pageable).map(PostResponseDto::from));
    }

    @GetMapping("/my")
    public Response<Page<PostResponseDto>> my(Pageable pageable, Authentication authentication) {
        return Response.success(feedService.my(pageable, authentication.getName()).map(PostResponseDto::from));
    }
}
