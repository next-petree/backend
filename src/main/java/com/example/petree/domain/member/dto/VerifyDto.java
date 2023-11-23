package com.example.petree.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * packageName    : com.example.petree.domain.member.dto
 * fileName       : VerifyDto
 * author         : 박수현
 * date           : 2023-08-10
 * description    : 레디스에 저장된 인증코드와 요청된 인증코드 일치를 위한 요청객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-10        박수현              최초 생성
 */
@Getter
@Setter
public class VerifyDto {
    @Schema(description = "전화번호", example = "010-1234-5678")
    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phoneNumber;
    @Schema(description = "인증코드", example = "60797")
    @NotBlank(message = "인증코드는 필수 입력 값입니다.")
    private String code;
}
