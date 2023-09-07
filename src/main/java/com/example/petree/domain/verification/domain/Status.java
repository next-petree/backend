package com.example.petree.domain.verification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 이지수
 * @date 2023-08-08
 * @description : 상태 값 클래스 다이어그램 반영
 * @return Status
 */
@Getter
@AllArgsConstructor
public enum Status {

    WAITING("승인대기"),
    APPROVAL("승인완료"),
    REFUSAL("승인거절");

    private final String status;
}
