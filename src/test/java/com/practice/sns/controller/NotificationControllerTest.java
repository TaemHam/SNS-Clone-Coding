package com.practice.sns.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.practice.sns.exception.ErrorCode;
import com.practice.sns.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    NotificationService notificationService;

    @Test
    @WithMockUser
    void 알림기능이_정상적으로_동작한다() throws Exception {
        // Given
        when(notificationService.getList(any(), any())).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/users/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 알림리스트_요청시_로그인하지_않은경우_에러반환() throws Exception {
        // Given
        when(notificationService.getList(any(), any())).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/users/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }
}
