package com.example.petree.domain.verification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @author 이지수
 * @date 2023-08-09
 * @description : 브리더 자격증 종류 enum 추가
 * @return certification
 */

@Getter
@AllArgsConstructor
public enum Certification {
    반려동물종합관리사("반려동물종합관리사"),
    반려동물행동교정사("반려동물행동교정사");

    private final String certification;

}
