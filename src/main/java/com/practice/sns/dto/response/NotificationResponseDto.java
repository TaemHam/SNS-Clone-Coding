package com.practice.sns.dto.response;

import com.practice.sns.domain.NotificationArgs;
import com.practice.sns.domain.constant.NotificationType;
import com.practice.sns.dto.NotificationDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private NotificationType notificationType;
    private NotificationArgs notificationArgs;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static NotificationResponseDto from(NotificationDto dto) {
        return new NotificationResponseDto(
                dto.getId(),
                dto.getNotificationType(),
                dto.getNotificationArgs(),
                dto.getNotificationType().getMessage(),
                dto.getRegisteredAt(),
                dto.getUpdatedAt(),
                dto.getDeletedAt()
        );
    }
}
