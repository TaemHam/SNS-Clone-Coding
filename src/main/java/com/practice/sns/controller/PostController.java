package com.practice.sns.controller;

import com.practice.sns.dto.PostDto;
import com.practice.sns.dto.request.PostCreateRequestDto;
import com.practice.sns.dto.request.PostModifyRequestDto;
import com.practice.sns.dto.response.PostResponseDto;
import com.practice.sns.dto.response.Response;
import com.practice.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{postId}")
    public Response<PostResponseDto> modify(@PathVariable Long postId, @RequestBody PostModifyRequestDto dto,
                                 Authentication authentication) {
        PostDto post = postService.modify(authentication.getName(), postId, dto.getTitle(), dto.getBody());
        return Response.success(PostResponseDto.from(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }
}
