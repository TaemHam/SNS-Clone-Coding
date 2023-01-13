package com.practice.sns.service;

import com.practice.sns.domain.Comment;
import com.practice.sns.domain.Like;
import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.dto.CommentDto;
import com.practice.sns.dto.PostDto;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.repository.CommentRepository;
import com.practice.sns.repository.LikeRepository;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    @Transactional
    public void comment(String userName, Long postId, String comment) {
        // 포스트를 찾는다
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                        String.format("Post ID %d does not exist", postId)));

        // 유저를 찾는다
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));

        //댓글을 저장한다
        commentRepository.save(Comment.of(comment, user, post));
    }

    @Transactional
    public Page<CommentDto> getComment(Pageable pageable, Long postId) {
        // 포스트를 찾는다
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                        String.format("Post ID %d does not exist", postId)));

        // 피드 목록을 페이지로 찾는다
        return commentRepository.findAllByPost(post, pageable).map(CommentDto::from);
    }
}