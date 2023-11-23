package com.example.petree.domain.breeder.schema;

import com.example.petree.domain.breeder.dto.BreederDetailDto;
import com.example.petree.domain.breeder.dto.BreederDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * packageName    : com.example.petree.domain.breeder.schema
 * fileName       : BreederSchema
 * author         : 박수현
 * date           : 2023-07-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-31        박수현              최초 생성
 */

@Data
public class BreederSchema{

    @Data
    public static class BreedersSchema {
        private Page<BreederDto> data;
    }

    @Data
    public static class BreederDetailSchema {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private BreederDetailDto data;

    }

}
