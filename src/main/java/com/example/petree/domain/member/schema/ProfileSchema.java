package com.example.petree.domain.member.schema;

import com.example.petree.domain.member.dto.ProfileDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * packageName    : com.example.petree.domain.member.schema
 * fileName       : ProfileSchema
 * author         : 박수현
 * date           : 2023-08-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-03        박수현              최초 생성
 */

@Data
public class ProfileSchema {

    @Data
    public static class ProfileImgSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private ProfileDto.ProfileImgResponseDto data;
    }

    @Data
    public static class PersonalInfoRequestDto200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private ProfileDto.PersonalInfoResponseDto data;
    }
}
