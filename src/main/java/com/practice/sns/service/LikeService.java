package com.practice.sns.service;

import com.practice.sns.domain.Like;
import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.repository.LikeRepository;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final LikeRepository likeRepository;

    @Transactional
    public void like(String userName, Long postId) {

        // 포스트를 찾는다
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                        String.format("Post ID %d does not exist", postId)));

        // 유저를 찾는다
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));

        // 좋아요를 이미 눌렀는지 확인한다
        likeRepository.findByUserAndPost(user, post).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED,
                    String.format("User name %s already liked post %d", userName, postId));
        });

        // 좋아요를 저장한다
        likeRepository.save(Like.of(user, post));
    }

    public int countLike(Long postId) {

        // 포스트를 찾는다
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                        String.format("Post ID %d does not exist", postId)));

        // 갯수를 반환한다
        return likeRepository.countAllByPost(post);
    }
}
