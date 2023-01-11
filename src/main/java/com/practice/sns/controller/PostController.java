package com.practice.sns.controller;

import com.practice.sns.dto.request.PostCreateRequestDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping()
    public Response<Void> create(@RequestBody PostCreateRequestDto dto, Authentication authentication) {
        postService.create(authentication.getName(), dto.getTitle(), dto.getBody());
        return Response.success();
    }
}
