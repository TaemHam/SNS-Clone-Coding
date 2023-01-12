package com.practice.sns.dto;

import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String body;
    private UserDto user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostDto from(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                UserDto.from(post.getUser()),
                post.getRegisteredAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }
}
