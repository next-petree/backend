package com.example.petree.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.example.petree.global
 * fileName       : CacheType
 * author         : 정세창
 * date           : 2023/08/16
 * description    : Caffeine 캐시 설정을 위한 Enum
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/16         정세창              init
 */
@Getter
@RequiredArgsConstructor
public enum CacheType {

    DOG_TYPE_KEYWORD("dogTypes", 1, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
