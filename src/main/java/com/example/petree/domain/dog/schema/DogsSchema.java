package com.example.petree.domain.dog.schema;

import com.example.petree.domain.dog.dto.SimpleDogDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * packageName    : com.example.petree.domain.dog.schema
 * fileName       : DogsSchema
 * author         : jsc
 * date           : 2023/07/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/25        jsc
 */
@Data
public class DogsSchema {
    @Data
    public static class DogsSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        Page<SimpleDogDto> data;
        @Schema(example = "null")
        private String msg;
    }

}
