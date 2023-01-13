package com.practice.sns.controller;

import com.practice.sns.dto.request.PostCommentRequestDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.service.CommentService;
import com.practice.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public Response<Void> comment(@RequestParam PostCommentRequestDto dto, @PathVariable Long postId,
                               Authentication authentication) {
        commentService.comment(authentication.getName(), postId, dto.getComment());
        return Response.success();
    }
}
