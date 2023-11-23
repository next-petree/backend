package com.example.petree.global.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.global.util
 * fileName       : PagingUtil
 * author         : jsc
 * date           : 2023/07/04
 * description    : 페이징 관련 유틸 함수 파일
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/04        jsc                paginator 추가
 */
@Component
public class PagingUtil {
    /**
     *
     * @param sourcePage: 기존 Page 객체
     * @param converter: 특정 객체를 해당 Dto로 변환하여 반환하는 함수
     * @return 변환된 Dto에 대한 Page 객체
     * @param <S>
     * @param <T>
     */
    public <S, T> Page<T> paginator(Page<S> sourcePage, Function<S, T> converter) {
        List<T> targetList = sourcePage.stream().map(converter).collect(Collectors.toList());
        return new PageImpl<>(targetList, sourcePage.getPageable(), sourcePage.getTotalElements());
    }
}
