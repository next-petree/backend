package com.example.petree.domain.matching.schema;

import com.example.petree.domain.dog.dto.DetailDogDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.example.petree.domain.matching.scheme
 * fileName       : AddMatchingScheme
 * author         : 정세창
 * date           : 2023/07/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/26         정세창              init
 */
@Data
public class PostResultSchema {

    @Data
    public static class PostResultSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema(example = "null")
        private String data;
        @Schema(example = "null")
        private String msg;
    }

}
