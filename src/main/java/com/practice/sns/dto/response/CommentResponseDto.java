package com.practice.sns.dto.response;

import com.practice.sns.dto.CommentDto;
import com.practice.sns.dto.PostDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String comment;
    private Long userId;
    private String userName;
    private Long postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentResponseDto from(CommentDto dto) {
        return new CommentResponseDto(
                dto.getId(),
                dto.getComment(),
                dto.getUserId(),
                dto.getUserName(),
                dto.getPostId(),
                dto.getRegisteredAt(),
                dto.getUpdatedAt(),
                dto.getDeletedAt()
        );
    }
}
