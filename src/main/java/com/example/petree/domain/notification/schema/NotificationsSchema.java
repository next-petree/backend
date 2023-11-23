package com.example.petree.domain.notification.schema;

import com.example.petree.domain.dog.dto.SimpleDogDto;
import com.example.petree.domain.notification.dto.NotificationDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.example.petree.domain.matching.scheme
 * fileName       : AddMatchingScheme
 * author         : 정세창
 * date           : 2023/07/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/26         정세창              init
 */
@Data
public class NotificationsSchema {

    @Data
    public static class NotificationSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        private List<NotificationDto> data;
        @Schema(example = "null")
        private String msg;
    }

}
