package com.example.petree.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Pattern;

/**
 * packageName    : com.example.petree.domain.member.dto
 * fileName       : MessageDto
 * author         : 박수현
 * date           : 2023-08-09
 * description    : 인증코드 보낼 전화번호를 담는 요청객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-09        박수현              최초 생성
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MessageDto {

    @Schema(description = "전화번호", example = "010-1234-5678")
    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    String to;
    //String content;
}
