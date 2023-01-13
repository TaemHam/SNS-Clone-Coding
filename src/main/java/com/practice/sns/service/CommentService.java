package com.practice.sns.service;

import com.practice.sns.domain.Like;
import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.repository.CommentRepository;
import com.practice.sns.repository.LikeRepository;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    }

}
