package com.example.petree.domain.member.schema;

import com.example.petree.domain.member.dto.LoginDto;
import com.example.petree.domain.member.dto.MemberDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * packageName    : com.example.petree.domain.member.schema
 * fileName       : LoginSchema
 * author         : 박수현
 * date           : 2023-08-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-01        박수현              최초 생성
 */

@Data
public class LoginSchema {

    @Data
    public static class LoginSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema LoginDto.LoginResponseDto data;
    }
}
