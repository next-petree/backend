package com.example.petree.domain.matching.schema;

import com.example.petree.domain.matching.dto.DetailMatchingOfBreederDto;
import com.example.petree.domain.matching.dto.SimpleMatchingOfBreederDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

/**
 * packageName    : com.example.petree.domain.matching.schema
 * fileName       : MatchingSchema
 * author         : 정세창
 * date           : 2023/07/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/26         정세창              init
 */
public class MatchingOfBreederSchema {

    @Data
    public static class MatchingSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        private DetailMatchingOfBreederDto data;
        @Schema(example = "")
        private String msg;
    }

    @Data
    public static class MatchingSchema500 {
        @Schema(example = "Error")
        private JsendStatus status;
        @Schema(example = "null")
        private String data;
        @Schema(example = "에러 메세지 내용")
        private String msg;
    }

}
