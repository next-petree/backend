package com.example.petree.domain.basic_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.basic_test.dto
 * fileName       : QuestionDto
 * author         : 박수현
 * date           : 2023-08-05
 * description    : 문항응답객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-05        박수현              최초 생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    @Schema(description = "문항 ID", example = "1")
    private Long id;
    @Schema(description = "문항 내용", example = "개를 크게 소형견, 중형견, 대형견으로 나눌 때 소형견에 해당하는 성견의 체중과 체고는 무엇인가?")
    private String questionText;
    @Schema(description = "선택지 리스트")
    private List<ChoiceDto> choices;
}
