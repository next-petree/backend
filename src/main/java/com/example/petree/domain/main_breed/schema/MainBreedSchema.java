package com.example.petree.domain.main_breed.schema;

import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.member.dto.MemberDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.main_breed.schema
 * fileName       : MainBreedSchema
 * author         : 박수현
 * date           : 2023-08-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-01        박수현              최초 생성
 */

@Data
public class MainBreedSchema {

    @Data
    public static class MainBreedListSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private List<MainBreedDto.MainBreedDtoResponse> data;
    }
}
