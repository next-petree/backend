package com.example.petree.domain.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.member.domain
 * fileName       : SocialType
 * author         : 박수현
 * date           : 2023-07-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */
public enum SocialType {

    @Schema(description = "카카오 로그인", example = "KAKAO")
    KAKAO;

    public static SocialType from(String type) {
        return SocialType.valueOf(type.toUpperCase());
    }

    public static boolean isSocialType(String type) {
        List<SocialType> socialTypes = Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.name().equals(type))
                .collect(Collectors.toList());
        return socialTypes.size() != 0;
    }
}
