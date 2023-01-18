package com.practice.sns.controller;

import com.practice.sns.dto.response.Response;
import com.practice.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts/{postId}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping()
    public Response<Void> like(@PathVariable Long postId, Authentication authentication) {
        likeService.like(authentication.getName(), postId);
        return Response.success();
    }

    @GetMapping()
    public Response<Long> countLike(@PathVariable Long postId) {
        return Response.success(likeService.countLike(postId));
    }
}
