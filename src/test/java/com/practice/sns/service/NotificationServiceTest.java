package com.practice.sns.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.practice.sns.domain.User;
import com.practice.sns.fixture.TestInfoFixture;
import com.practice.sns.repository.NotificationRepository;
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

    @Test
    void 알림목록_요청이_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.of(fixture.getUserId(), fixture.getUserName(), fixture.getPassword());
        when(notificationRepository.findAllByUserId(any(), any())).thenReturn(Page.empty());

        // When & Then
        assertDoesNotThrow(() -> notificationService.getList(fixture.getUserId(), pageable));
    }
}