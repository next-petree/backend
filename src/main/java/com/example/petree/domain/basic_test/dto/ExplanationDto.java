package com.example.petree.domain.basic_test.dto;

import com.example.petree.domain.basic_test.domain.Explanation;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * packageName    : com.example.petree.domain.basic_test.dto
 * fileName       : ExplanationDto
 * author         : 박수현
 * date           : 2023-08-05
 * description    : 해설을 담은 응답객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-05        박수현              최초 생성
 */

@Data
@Builder
public class ExplanationDto {

    @Schema(description = "문항번호", example = "1")
    private Long questionId;
    @Schema(description = "해설내용", example = "협회마다 차이는 조금씩 있지만 소형견은 몸무게 10kg 이하에 체고 40cm 이하의 개를 말한다. \n" +
            "체중 3Kg 이하일 경우 초소형견이라고 하기도 한다.")
    private String explanationText;
    @Schema(description = "실제 정답 번호", example = "2")
    private Long correctChoiceId;

    public static ExplanationDto createExplanataionDto(Explanation explanation, Long correctChoiceId) {
        return new ExplanationDto(
                explanation.getId(),
                explanation.getExplanationText(),
                correctChoiceId
        );
    }
}
