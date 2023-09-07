package com.example.petree.domain.member.schema;

import com.example.petree.domain.dog.dto.DetailDogDto;
import com.example.petree.domain.member.dto.MemberDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * packageName    : com.example.petree.domain.member.schema
 * fileName       : MemberSchema
 * author         : 박수현
 * date           : 2023-08-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-01        박수현              최초 생성
 */

@Data
public class MemberSchema {

    @Data
    public static class MemberSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema MemberDto.MemberDtoReponse data;
    }

    @Data
    public static class EmailFindSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema MemberDto.IdFindResponseDto data;
    }
}
