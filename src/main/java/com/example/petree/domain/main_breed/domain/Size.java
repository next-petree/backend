package com.example.petree.domain.main_breed.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Size {
    UNKNOWN("정보없음"),
    EXTRA_SMALL("특소형견"),
    SMALL("소형견"),
    MEDIUM("중형견"),
    LARGE("대형견");

    private final String name;
}
