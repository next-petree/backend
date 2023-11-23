package com.example.petree.global.web.external.kakao.service;

import com.example.petree.domain.member.domain.SocialType;
import com.example.petree.global.jwt.constant.GrantType;
import com.example.petree.global.web.external.kakao.client.KakaoUserInfoClient;
import com.example.petree.global.web.external.kakao.dto.KakaoUserInfoResponseDto;
import com.example.petree.global.web.external.model.OAuthAttributes;
import com.example.petree.global.web.external.service.SocialLoginApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * packageName    : com.example.petree.global.web.external.kakao.service
 * fileName       : KakaoLoginApiServiceImpl
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 소셜플랫폼 중 카카오로부터 받은 회원정보 조회
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoLoginApiServiceImpl implements SocialLoginApiService {

    private final KakaoUserInfoClient kakaoUserInfoClient;
    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf8";

    /**
     * @author 박수현
     * @date 2023-07-29
     * @description : 카카오로부터 제공받은 AT를 기반으로 카카오의 사용자정보 조회,
     *                이메일은 필수로 제공해주는 정보가 아니기에 이메일 대신 카카오에서 기본으로 제공해주는 ID식별자값을 이메일 대신 반환받도록 구현
     * @return OAuthAttributes
     */

    @Override
    public OAuthAttributes getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto kakaoUserInfoResponseDto = kakaoUserInfoClient.getKakaoUserInfo(CONTENT_TYPE,
                GrantType.BEARER.getType() + " " + accessToken);
        KakaoUserInfoResponseDto.KakaoAccount kakaoAccount = kakaoUserInfoResponseDto.getKakaoAccount();
        String email = kakaoAccount.getEmail();

        return OAuthAttributes.builder()
                .email(!StringUtils.hasText(email) ? kakaoUserInfoResponseDto.getId() : email)
                .nickname(kakaoAccount.getProfile().getNickname())
                .profile(kakaoAccount.getProfile().getThumbnailImageUrl())
                .socialType(SocialType.KAKAO)
                .build();
    }
}
