package com.practice.sns.service;

import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.dto.PostDto;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(String userName, String title, String body) {
        // 유저를 찾는다
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));

        // 포스트를 저장한다
        postRepository.save(Post.of(title, body, user));
    }

    @Transactional
    public PostDto modify(String userName, Long postId, String title, String body) {

        // 유저를 찾는다
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));

        // 포스트를 찾는다
        Post post = postRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("Post ID %d does not exist", postId)));

        // 포스트 작성자를 확인한다
        if (!post.getUser().equals(user)) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("User Name %s has no permission to modify Post ID %d", userName, postId));
        }

        // 포스트를 저장한다
        post.setTitle(title);
        post.setBody(body);

        return PostDto.from(postRepository.saveAndFlush(post));
    }
}
