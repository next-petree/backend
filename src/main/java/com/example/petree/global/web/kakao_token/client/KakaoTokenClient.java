package com.example.petree.global.web.kakao_token.client;

import com.example.petree.global.web.kakao_token.dto.KakaoTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * packageName    : com.example.petree.global.web.kakao_token.client
 * fileName       : KakaoTokenClient
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 인가코드를 기반으로 카카오에게 AT요청
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoTokenClient {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
    KakaoTokenDto.Response requestKakaoToken(@RequestHeader("Content-Type") String contentType,
                                            @SpringQueryMap KakaoTokenDto.Request request
    );

}
