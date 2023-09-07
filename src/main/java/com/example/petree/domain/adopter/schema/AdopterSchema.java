package com.example.petree.domain.adopter.schema;

import com.example.petree.domain.adopter.dto.AdopterDetailDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * packageName    : com.example.petree.domain.adopter.schema
 * fileName       : AdopterSchema
 * author         : 박수현
 * date           : 2023-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-27        박수현              최초 생성
 */

@Data
public class AdopterSchema {

    @Schema(example = "SUCCESS")
    private JsendStatus status;
    @Schema
    private AdopterDetailDto data;
}
