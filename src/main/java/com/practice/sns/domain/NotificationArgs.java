package com.practice.sns.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationArgs {
    // 알람을 발생시킨 사람
    private Long fromUserId;
    // 알람이 발생한 곳
    private Long targetId;

}
