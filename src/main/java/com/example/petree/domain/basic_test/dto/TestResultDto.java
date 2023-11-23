package com.example.petree.domain.basic_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.basic_test.dto
 * fileName       : TestResultDto
 * author         : 박수현
 * date           : 2023-08-05
 * description    : 사용자가 문항과 문항에 대해 고른 선택지(TestResultRequestDto) / 제출 후 반환하는 응답객체(TestResultResponseDto)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-05        박수현              최초 생성
 */

@Data
public class TestResultDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResultRequestDto {
        private List<TestAnswerDto> answers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResultResponseDto {
        @Schema(description = "점수", example = "88")
        private int score;
        @Schema(description = "통과여부", example = "true")
        private boolean passed;
        private List<ExplanationDto> explanations;
    }

}
