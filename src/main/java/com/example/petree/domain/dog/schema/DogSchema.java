package com.example.petree.domain.dog.schema;

import com.example.petree.domain.dog.dto.DetailDogDto;
import com.example.petree.domain.dog.dto.SimpleDogDto;
import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

/**
 * packageName    : com.example.petree.domain.dog.schema
 * fileName       : DogSchema
 * author         : jsc
 * date           : 2023/07/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/25        jsc
 */
@Data
public class DogSchema{

    @Data
    public static class DogSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        DetailDogDto data;
        @Schema(example = "null")
        private String msg;
    }

}
