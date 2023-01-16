package com.practice.sns.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {

    NEW_COMMENT_ON_POST("New comment!"),
    NEW_LIKE_ON_POST("New Like!"),
    ;

    private final String message;
}
