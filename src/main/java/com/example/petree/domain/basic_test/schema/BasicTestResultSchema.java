package com.example.petree.domain.basic_test.schema;

import com.example.petree.domain.basic_test.dto.BasicTestDto;
import com.example.petree.domain.basic_test.dto.TestResultDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * packageName    : com.example.petree.domain.basic_test.schema
 * fileName       : BasicTestResultSchema
 * author         : 박수현
 * date           : 2023-08-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-06        박수현              최초 생성
 */

@Data
public class BasicTestResultSchema {

    @Data
    public static class BasicTestResultSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private TestResultDto.TestResultResponseDto data;
    }
}
