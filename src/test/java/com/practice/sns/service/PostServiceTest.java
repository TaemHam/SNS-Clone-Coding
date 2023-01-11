package com.practice.sns.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.fixture.UserEntityFixture;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    void 포스트작성이_성공한_경우() {
        // Given
        String userName = "name";
        String password = "password";
        String title = "title";
        String body = "body";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        // When & Then
        assertDoesNotThrow(() -> postService.create(userName, title, body));
    }

    @Test
    void 포스트작성시_요청한유저가_존재하지않는경우() {
        // Given
        String userName = "name";
        String password = "password";
        String title = "title";
        String body = "body";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        // When & Then
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
                () -> postService.create(userName, title, body));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}