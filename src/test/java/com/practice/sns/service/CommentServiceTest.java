package com.practice.sns.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.practice.sns.domain.Comment;
import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.fixture.TestInfoFixture;
import com.practice.sns.repository.CommentRepository;
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
    private CommentRepository commentRepository;
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
        assertDoesNotThrow(
                () -> commentService.comment(fixture.getUserName(), fixture.getPostId(), fixture.getComment()));
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

    @Test
    void 댓글_수정이_성공한_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.of(fixture.getUserId(), fixture.getUserName(), fixture.getPassword());
        Post post = Post.of(fixture.getPostId(), fixture.getTitle(), fixture.getBody(), user);
        Comment comment = Comment.of(fixture.getCommentId(), fixture.getComment(), user, post);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(comment));

        // When & Then
        assertDoesNotThrow(
                () -> commentService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId(),
                        fixture.getComment()));
    }

    @Test
    void 댓글_수정시_댓글이_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class,
                () -> commentService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId(),
                        fixture.getComment()));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 댓글_수정시_포스트가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mock(User.class)));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(mock(Comment.class)));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () ->
                commentService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId(),
                        fixture.getComment()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 댓글_수정시_유저가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(mock(Comment.class)));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class,
                () -> commentService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId(),
                        fixture.getComment()));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 댓글_수정시_작성자와_일치하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        Post mockPost = mock(Post.class);
        Comment mockComment = mock(Comment.class);
        User mockUser = mock(User.class);
        User mockUser2 = mock(User.class);
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUser));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(mockComment));
        when(mockComment.getUser()).thenReturn(mockUser2);

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class,
                () -> commentService.modify(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId(),
                        fixture.getComment()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void 댓글_삭제가_성공한_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.of(fixture.getUserId(), fixture.getUserName(), fixture.getPassword());
        Post post = Post.of(fixture.getPostId(), fixture.getTitle(), fixture.getBody(), user);
        Comment comment = Comment.of(fixture.getCommentId(), fixture.getComment(), user, post);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(comment));

        // When & Then
        assertDoesNotThrow(
                () -> commentService.delete(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId()));
    }

    @Test
    void 댓글_삭제시_댓글이_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () ->
                commentService.delete(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId()));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 댓글_삭제시_포스트가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mock(User.class)));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(mock(Comment.class)));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () ->
                commentService.delete(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 댓글_삭제시_유저가_존재하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(mock(Comment.class)));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class,
                () -> commentService.delete(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId()));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void 댓글_삭제시_작성자와_일치하지_않는_경우() {
        // Given
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        Post mockPost = mock(Post.class);
        Comment mockComment = mock(Comment.class);
        User mockUser = mock(User.class);
        User mockUser2 = mock(User.class);
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUser));
        when(commentRepository.findById(fixture.getCommentId())).thenReturn(Optional.of(mockComment));
        when(mockComment.getUser()).thenReturn(mockUser2);

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class,
                () -> commentService.delete(fixture.getUserName(), fixture.getPostId(), fixture.getCommentId()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
}