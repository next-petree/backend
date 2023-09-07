package com.example.petree.domain.main_breed.schema;

import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.main_breed.schema
 * fileName       : AllDogTypesSchema
 * author         : jsc
 * date           : 2023/08/14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/14        jsc
 */
@Data
public class AllDogTypesSchema {

    @Data
    public static class AllDogTypeSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private List<MainBreedDto> data;
    }
}
