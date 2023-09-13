package com.example.petree.domain.matching.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.example.petree.domain.matching.domain
 * fileName       : SearchType
 * author         : qkrtn_ulqpbq2
 * date           : 2023-09-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-12        qkrtn_ulqpbq2       최초 생성
 */

@Getter
@AllArgsConstructor
public enum SearchType {

    @Schema(description = "강아지 이름", example = "쫑이")
    name("강아지 이름"),
    @Schema(description = "견종", example = "골든 리트리버")
    type("견종");

    private final String searchType;
}
