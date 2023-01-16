package com.practice.sns.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.practice.sns.domain.Like;
import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.fixture.TestInfoFixture;
import com.practice.sns.repository.LikeRepository;
import com.practice.sns.repository.NotificationRepository;
import com.practice.sns.repository.PostRepository;
import com.practice.sns.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;
    @MockBean
    private NotificationRepository notificationRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    void 알림목록_요청이_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.of(fixture.getUserId(), fixture.getUserName(), fixture.getPassword());
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(notificationRepository.findAllByUser(eq(user), any())).thenReturn(Page.empty());

        // When & Then
        assertDoesNotThrow(() -> notificationService.getList(fixture.getUserName(), pageable));
    }

    @Test
    void 알림목록_요청시_요청한_유저가_존재하지_않는_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () ->
                notificationService.getList(fixture.getUserName(), pageable));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}