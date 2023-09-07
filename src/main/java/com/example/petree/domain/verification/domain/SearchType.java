package com.example.petree.domain.verification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum SearchType {

    EMAIL("아이디"),
    NICKNAME("닉네임"),
    SUBMITDATE("제출일"),
    CERTIFICATION("자격증"),
    STATUS("상태"),
    WHOLE("전체");
    private final String searchType;
}