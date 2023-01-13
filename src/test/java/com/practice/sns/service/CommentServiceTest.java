package com.practice.sns.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.practice.sns.domain.Like;
import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.fixture.TestInfoFixture;
import com.practice.sns.repository.CommentRepository;
import com.practice.sns.repository.LikeRepository;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    void 댓글_요청이_성공한_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.of(fixture.getUserId(), fixture.getUserName(), fixture.getPassword());
        Post post = Post.of(fixture.getPostId(), fixture.getTitle(), fixture.getBody(), user);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(post));

        // When & Then
        assertDoesNotThrow(() -> commentService.comment(fixture.getUserName(), fixture.getPostId(), fixture.getComment()));
    }

    @Test
    void 댓글_요청시_포스트가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () ->
                commentService.comment(fixture.getUserName(), fixture.getPostId(), fixture.getComment()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 댓글_요청시_유저가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class,
                () -> commentService.comment(fixture.getUserName(), fixture.getPostId(), fixture.getComment()));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}