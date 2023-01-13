package com.practice.sns.dto;

import com.practice.sns.domain.Comment;
import com.practice.sns.domain.Post;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String comment;
    private Long userId;
    private String userName;
    private Long postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getUser().getId(),
                comment.getUser().getUserName(),
                comment.getPost().getId(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt()
        );
    }
}
