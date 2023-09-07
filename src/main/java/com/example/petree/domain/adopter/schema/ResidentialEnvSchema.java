package com.example.petree.domain.adopter.schema;

import com.example.petree.domain.adopter.dto.ResidentialEnvDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.adopter.schema
 * fileName       : ResidentialEnvSchema
 * author         : 정세창
 * date           : 2023/08/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/21        정세창               init
 */
public class ResidentialEnvSchema {

    @Data
    public static class ResidentialEnvSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private List<ResidentialEnvDto.EnvResponseDto> data;
    }
}
