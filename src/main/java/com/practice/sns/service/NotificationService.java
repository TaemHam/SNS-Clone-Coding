package com.practice.sns.service;

import com.practice.sns.dto.NotificationDto;
import com.practice.sns.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Page<NotificationDto> getList(Long userId, Pageable pageable) {
        return notificationRepository.findAllByUserId(userId, pageable).map(NotificationDto::from);
    }
}
