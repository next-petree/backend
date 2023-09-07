package com.example.petree.domain.dog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    AVAILABLE("입양가능"),
    UNDERWAY("진행중"),
    DONE("완료");

    private final String status;
}
