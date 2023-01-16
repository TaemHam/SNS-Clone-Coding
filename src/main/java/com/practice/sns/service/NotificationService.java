package com.practice.sns.service;

import com.practice.sns.domain.Notification;
import com.practice.sns.domain.User;
import com.practice.sns.dto.NotificationDto;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.repository.NotificationRepository;
import com.practice.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public Page<NotificationDto> getList(String userName, Pageable pageable) {
        // 유저를 찾는다
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));
        System.out.println(user.getId());

        return notificationRepository.findAllByUser(user, pageable).map(NotificationDto::from);
    }
}
