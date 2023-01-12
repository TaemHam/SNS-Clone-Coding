package com.practice.sns.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sns.dto.PostDto;
import com.practice.sns.dto.UserDto;
import com.practice.sns.dto.request.PostCreateRequestDto;
import com.practice.sns.dto.request.PostModifyRequestDto;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.service.PostService;
import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @Test
    @WithMockUser
    void 포스트작성이_정상적으로_동작함() throws Exception {
        // Given
        String title = "title";
        String body = "body";

        // When & Then
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequestDto(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트작성시_로그인_하지_않은경우_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";

        // When & Then
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequestDto(title, body)))
                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트수정이_정상적으로_동작함() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        UserDto userDto = new UserDto(null, "name", null, null, null, null, null);
        PostDto postDto = new PostDto(1L, "title", "body", userDto, null, null, null);
        when(postService.modify(any(), any(), eq(title), eq(body))).thenReturn(postDto);

        // When & Then
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequestDto(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트수정시_로그인한상태가_아니라면_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";

        // When & Then
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequestDto("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }


    @Test
    @WithMockUser
    void 포스트수정시_본인이_작성한_글이_아니라면_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(any(), eq(1L), eq("title"), eq("body"));

        // When & Then
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequestDto("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트수정시_수정하려는글이_없다면_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(any(), eq(1L), eq("title"), eq("body"));

        // When & Then
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequestDto("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트수정시_데이터베이스_에러_발생시_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        doThrow(new SnsApplicationException(ErrorCode.DATABASE_ERROR)).when(postService).modify(any(), eq(1L), eq("title"), eq("body"));

        // When & Then
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequestDto("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트삭제가_정상적으로_동작함() throws Exception {
        // Given
        UserDto userDto = new UserDto(null, "name", null, null, null, null, null);
        PostDto postDto = new PostDto(1L, "title", "body", userDto, null, null, null);

        // When & Then
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트삭제시_로그인한상태가_아니라면_에러반환() throws Exception {
        // Given
        UserDto userDto = new UserDto(null, "name", null, null, null, null, null);
        PostDto postDto = new PostDto(1L, "title", "body", userDto, null, null, null);

        // When & Then
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트삭제시_본인이_작성한_글이_아니라면_에러반환() throws Exception {
        // Given
        UserDto userDto = new UserDto(null, "name", null, null, null, null, null);
        PostDto postDto = new PostDto(1L, "title", "body", userDto, null, null, null);
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        // When & Then
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트삭제시_삭제하려는글이_없다면_에러반환() throws Exception {
        // Given
        UserDto userDto = new UserDto(null, "name", null, null, null, null, null);
        PostDto postDto = new PostDto(1L, "title", "body", userDto, null, null, null);
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        // When & Then
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트삭제시_데이터베이스_에러_발생시_에러반환() throws Exception {
        // Given
        UserDto userDto = new UserDto(null, "name", null, null, null, null, null);
        PostDto postDto = new PostDto(1L, "title", "body", userDto, null, null, null);
        doThrow(new SnsApplicationException(ErrorCode.DATABASE_ERROR)).when(postService).delete(any(), any());

        // When & Then
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }



}


