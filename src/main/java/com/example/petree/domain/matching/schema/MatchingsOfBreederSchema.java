package com.example.petree.domain.matching.schema;

import com.example.petree.domain.dog.dto.DetailDogDto;
import com.example.petree.domain.matching.dto.SimpleMatchingOfBreederDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

/**
 * packageName    : com.example.petree.domain.matching.schema
 * fileName       : MatchingsOfBreederSchema
 * author         : jsc
 * date           : 2023/07/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/26        jsc
 */
public class MatchingsOfBreederSchema{

    @Data
    public static class MatchingsSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        private Page<SimpleMatchingOfBreederDto> data;
        @Schema(example = "")
        private String msg;
    }


}
