package com.practice.sns.service;

import com.practice.sns.domain.User;
import com.practice.sns.dto.PostDto;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Page<PostDto> list(Pageable pageable) {
        // 피드 목록을 페이지로 찾는다
        return postRepository.findAll(pageable).map(PostDto::from);
    }

    @Transactional
    public Page<PostDto> my(Pageable pageable, String userName) {
        // 유저를 찾는다
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));

        // 피드 목록을 페이지로 찾는다
        return postRepository.findAllByUser(user, pageable).map(PostDto::from);
    }
}
