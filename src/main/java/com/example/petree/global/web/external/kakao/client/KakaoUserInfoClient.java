package com.example.petree.global.web.external.kakao.client;

import com.example.petree.global.web.external.kakao.dto.KakaoUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * packageName    : com.example.petree.global.web.external.kakao.client
 * fileName       : KakaoUserInfoClient
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 카카오 서버로부터 정보 받기
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@FeignClient(url = "https://kapi.kakao.com", name = "kakaoUserInfoClient")
public interface KakaoUserInfoClient {
    @GetMapping(value = "/v2/user/me", consumes = "application/json")
    KakaoUserInfoResponseDto getKakaoUserInfo(@RequestHeader("Content-type") String contentType,
                                              @RequestHeader("Authorization") String accessToken);
}
