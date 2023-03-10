package com.practice.sns.fixture;

import lombok.Builder;
import lombok.Data;

public class TestInfoFixture {

    public static TestInfo get() {
        return TestInfo.builder()
                .postId(1L)
                .userId(1L)
                .commentId(1L)
                .userName("name")
                .password("password")
                .title("title")
                .body("body")
                .comment("comment")
                .build();
    }

    @Data
    @Builder
    public static class TestInfo {
        private Long postId;
        private Long userId;
        private Long commentId;
        private String userName;
        private String password;
        private String title;
        private String body;
        private String comment;
    }
}
