package com.example.petree.global.web.external.service;

import com.example.petree.domain.member.domain.SocialType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * packageName    : com.example.petree.global.web.external.service
 * fileName       : SocialLoginApiServiceFactory
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 현재 카카오로그인만 진행하지만 앞으로 구글이나 네이버를 사용할 수도 있음.
 *                  SocialType에 따라 구현체를 반환
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현             최초 생성
 */

@Slf4j
@Service
public class SocialLoginApiServiceFactory {

    private static Map<String, SocialLoginApiService> socialLoginApiServices;

    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
        this.socialLoginApiServices = socialLoginApiServices;
    }

    public static SocialLoginApiService getSocialLoginApiService(SocialType socialType) {
        String socialLoginApiServiceBeanName = "";

        if(SocialType.KAKAO.equals(socialType)) {
            socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
        }
        log.info("객체 생성 후 : " + socialLoginApiServices.get(socialLoginApiServiceBeanName));
        return socialLoginApiServices.get(socialLoginApiServiceBeanName);
    }
}
