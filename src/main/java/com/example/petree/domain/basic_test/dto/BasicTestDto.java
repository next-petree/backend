package com.example.petree.domain.basic_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.basic_test.dto
 * fileName       : BasicTestDto
 * author         : 박수현
 * date           : 2023-07-30
 * description    : 기초지식테스트에 대한 응답객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-30        박수현              최초 생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicTestDto {

    private List<QuestionDto> questions;
}
