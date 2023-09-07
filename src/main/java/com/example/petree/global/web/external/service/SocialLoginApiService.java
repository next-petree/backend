package com.example.petree.global.web.external.service;

import com.example.petree.global.web.external.model.OAuthAttributes;

/**
 * packageName    : com.example.petree.global.web.external.service
 * fileName       : SocialLoginApiService
 * author         : 박수현
 * date           : 2023-07-11
 * description    : AT기반으로 OAuthAttributes객체 받기,
 *                  소셜 플랫폼마다 받아오는 회원정보의 형태가 다름.
 *                  어떤 소셜 플랫폼이든 소셜플랫폼으로부터 회원정보를 동일하게 OAuthAttributes객체로 반환하기 위한 인터페이스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */
public interface SocialLoginApiService {

    OAuthAttributes getUserInfo(String accessToken);
}
