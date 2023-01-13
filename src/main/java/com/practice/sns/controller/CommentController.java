package com.practice.sns.controller;

import com.practice.sns.dto.request.CommentRequestDto;
import com.practice.sns.dto.response.CommentResponseDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public Response<Void> comment(@RequestBody CommentRequestDto dto, @PathVariable Long postId,
                                  Authentication authentication) {
        commentService.comment(authentication.getName(), postId, dto.getComment());
        return Response.success();
    }

    @GetMapping()
    public Response<Page<CommentResponseDto>> getComment(Pageable pageable, @PathVariable Long postId) {
        return Response.success(commentService.getComment(pageable, postId).map(CommentResponseDto::from));
    }
}
