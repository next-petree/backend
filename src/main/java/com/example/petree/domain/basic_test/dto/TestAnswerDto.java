package com.example.petree.domain.basic_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;

/**
 * packageName    : com.example.petree.domain.basic_test.dto
 * fileName       : TestAnswerDto
 * author         : 박수현
 * date           : 2023-08-05
 * description    : 사용자가 제출한 것으로, 문항과 선택지를 담은 객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-05        박수현              최초 생성
 */

@Data
public class TestAnswerDto {

    @Schema(description = "문항 번호", example = "1")
    private Long questionId;
    @Schema(description = "문항에 대해 사용자가 고른 선택지 번호", example = "1")
    private Long selectedChoiceId;
}
