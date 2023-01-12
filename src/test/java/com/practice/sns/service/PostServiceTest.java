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
import com.practice.sns.fixture.TestInfoFixture;
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
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(UserEntityFixture.get(
                fixture.getUserName(), fixture.getPassword())));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        // When & Then
        assertDoesNotThrow(() -> postService.create(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));
    }

    @Test
    void 포스트작성시_요청한유저가_존재하지않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        // When & Then
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
                () -> postService.create(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트_수정이_성공한_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.of(fixture.getUserId(), fixture.getUserName(), fixture.getPassword());
        Post post = Post.of(fixture.getPostId(), fixture.getTitle(), fixture.getBody(), user);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(post));
        when(postRepository.saveAndFlush(any())).thenReturn(post);

        // When & Then
        assertDoesNotThrow(() -> postService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
    }

    @Test
    void 포스트_수정시_포스트가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () ->
                postService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_유저가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_포스트_작성자와_유저가_일치하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);
        User mockUser2 = mock(User.class);
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUser));
        when(mockPost.getUser()).thenReturn(mockUser2);

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void 포스트_삭제가_성공한_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.of(fixture.getUserId(), fixture.getUserName(), fixture.getPassword());
        Post post = Post.of(fixture.getPostId(), fixture.getTitle(), fixture.getBody(), user);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(post));

        // When & Then
        assertDoesNotThrow(() -> postService.delete(fixture.getUserName(), fixture.getPostId()));
    }

    @Test
    void 포스트_삭제시_포스트가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () ->
                postService.delete(fixture.getUserName(), fixture.getPostId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_삭제시_유저가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.delete(fixture.getUserName(), fixture.getPostId()));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void 포스트_삭제시_포스트_작성자와_유저가_일치하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);
        User mockUser2 = mock(User.class);
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUser));
        when(mockPost.getUser()).thenReturn(mockUser2);

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.delete(fixture.getUserName(), fixture.getPostId()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
}