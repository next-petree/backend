package com.example.petree.domain.notification.service;

import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.notification.domain.Notification;
import com.example.petree.domain.notification.dto.NotificationDto;
import com.example.petree.domain.notification.repository.NotificationRepository;
import com.example.petree.global.error.ErrorCode;
import com.example.petree.global.error.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.matching.service
 * fileName       : notificationService
 * author         : 정세창
 * date           : 2023/07/17
 * description    : 알림 관련 서비스 로직 코드
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/17         정세창              init
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public List<NotificationDto> getNotifications(Member member) {
        Sort sort = Sort.by("registrationTime").descending();
        List<Notification> notifications = notificationRepository
                .findByMemberAndDeleteFlag(member, false, sort);

        return notifications.stream().map(NotificationDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void changeNotificationStatus(Long notificationId) {
        Notification notification = notificationRepository
                .findById(notificationId).orElseThrow(() -> new ServerException(ErrorCode.NULL_EXCEPTION));

        notification.setDeleteFlag(true);
        notificationRepository.save(notification);
    }
}
