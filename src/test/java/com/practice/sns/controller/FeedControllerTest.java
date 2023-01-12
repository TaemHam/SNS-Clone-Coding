package com.practice.sns.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.service.FeedService;
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
public class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    FeedService feedService;

    @Test
    @WithMockUser
    void 피드목록요청이_정상적으로_동작함() throws Exception {
        // Given
        when(feedService.list(any())).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 피드목록요청시_로그인_하지_않은경우_에러반환() throws Exception {
        // Given
        when(feedService.list(any())).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 나의_피드목록요청이_정상적으로_동작함() throws Exception {
        // Given
        when(feedService.my(any(), any())).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/feed/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 나의_피드목록요청시_로그인_하지_않은경우_에러반환() throws Exception {
        // Given
        when(feedService.my(any(), any())).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/feed/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }
}


