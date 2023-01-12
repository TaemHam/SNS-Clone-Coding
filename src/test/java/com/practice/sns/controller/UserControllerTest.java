package com.practice.sns.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sns.dto.UserDto;
import com.practice.sns.dto.request.UserJoinRequestDto;
import com.practice.sns.dto.request.UserLoginRequestDto;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void 회원가입이_정상적으로_동작한다() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";
        when(userService.join(userName, password)).thenReturn(mock(UserDto.class));

        // When & Then
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequestDto(userName, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러반환() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";
        when(userService.join(userName, password)).thenThrow(
                new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME));

        // When & Then
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequestDto(userName, password)))
                ).andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()));
    }

    @Test
    void 로그인이_정상적으로_동작한다() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";
        when(userService.login(userName, password)).thenReturn("test_token");

        // When & Then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequestDto(userName, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 로그인시_없는_userName을_입력할경우_에러반환() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";
        when(userService.login(userName, password)).thenThrow(
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        // When & Then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequestDto(userName, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void 로그인시_틀린_password를_입력할경우_에러반환() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";
        when(userService.login(userName, password)).thenThrow(
                new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

        // When & Then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequestDto(userName, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
