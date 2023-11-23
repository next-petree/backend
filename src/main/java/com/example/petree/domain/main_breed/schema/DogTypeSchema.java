package com.example.petree.domain.main_breed.schema;

import com.example.petree.domain.main_breed.dto.DogTypeDto;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.main_breed.schema
 * fileName       : DogTypeSchema
 * author         : 박수현
 * date           : 2023-08-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-14        박수현              최초 생성
 */

@Data
public class DogTypeSchema {

    @Data
    public static class DogTypeListSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private List<DogTypeDto> data;
    }

    @Data
    public static class DogTypeSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private DogTypeDto data;
    }
}
