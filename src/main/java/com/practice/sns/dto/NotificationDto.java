package com.practice.sns.dto;

import com.practice.sns.domain.Notification;
import com.practice.sns.domain.NotificationArgs;
import com.practice.sns.domain.constant.NotificationType;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private UserDto user;
    private NotificationType notificationType;
    private NotificationArgs notificationArgs;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static NotificationDto from(Notification entity) {
        return new NotificationDto(
                entity.getId(),
                UserDto.from(entity.getUser()),
                entity.getNotificationType(),
                entity.getNotificationArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
