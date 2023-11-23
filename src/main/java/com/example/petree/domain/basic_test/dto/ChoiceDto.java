package com.example.petree.domain.basic_test.dto;

import com.example.petree.domain.basic_test.domain.Choice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.basic_test.dto
 * fileName       : ChoiceDto
 * author         : 박수현
 * date           : 2023-08-05
 * description    : 선택지응답객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-05        박수현              최초 생성
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDto {

    @Schema(description = "선택지 ID", example = "1")
    private Long id;
    @Schema(description = "선택지 내용", example = "체중 10Kg 이하, 체고 40cm 미만")
    private String choiceText;
}
