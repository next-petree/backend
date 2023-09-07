package com.example.petree.domain.notification.dto;

import com.example.petree.domain.notification.domain.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * packageName    : com.example.petree.domain.notification.dto
 * fileName       : notificationDto
 * author         : 정세창
 * date           : 2023/07/17
 * description    : 알림에 대한 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/17        정세창               init
 */
@Getter
@NoArgsConstructor
public class NotificationDto {

    @Schema(description = "알림 식별자 id", example = "1")
    private Long id;
    @Schema(description = "알림 내용", example = "브리더(김철수)가 입양 희망 요청을 수락하셨습니다.")
    private String content;
    @Schema(description = "알림 등록 시간", example = "2023-07-21 23:44:21")
    private String dateTime;

    public NotificationDto(Notification notification) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.id = notification.getId();
        this.content = notification.getContent();
        this.dateTime = notification.getRegistrationTime().format(formatter);
    }
}
