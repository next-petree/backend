package com.example.petree.domain.notification.repository;

import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.notification.domain.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.notification.repository
 * fileName       : NotificationRepository
 * author         : jsc
 * date           : 2023/07/16
 * description    : 알림 엔티티 repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/16         정세창              init
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberAndDeleteFlag(Member member, Boolean deleteFlag, Sort sort);
}

