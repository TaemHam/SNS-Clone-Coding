package com.practice.sns.dto.response;

import com.practice.sns.dto.PostDto;
import com.practice.sns.dto.UserDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String body;
    private UserResponseDto user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostResponseDto from(PostDto postDto) {
        return new PostResponseDto(
                postDto.getId(),
                postDto.getTitle(),
                postDto.getBody(),
                UserResponseDto.from(postDto.getUser()),
                postDto.getRegisteredAt(),
                postDto.getUpdatedAt(),
                postDto.getDeletedAt()
        );
    }
}
