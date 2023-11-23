package com.example.petree.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.member.dto
 * fileName       : SmsDto
 * author         : 박수현
 * date           : 2023-08-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-09        박수현              최초 생성
 */

@Data
public class SmsDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class SmsRequestDto {
        String type;
        String contentType;
        String countryCode;
        String from;
        String content;
        List<MessageDto> messages;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class SmsResponseDto {
        @Schema(description = "요청아이디", example = "RSSA-1691631061413-4514-57656329-roxmBCUD")
        private String requestId;
        @Schema(description = "요청시간", example = "2023-08-10T10:31:01.413")
        private LocalDateTime requestTime;
        @Schema(description = "인증코드", example = "91119")
        private String smsConfirmNum;
    }
}
