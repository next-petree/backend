package com.example.petree.domain.dog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {

    NAME("이름"),
    TYPE("견종"),
    WHOLE("전체");

    private final String searchType;
}