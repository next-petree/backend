package com.example.petree.global.web.oauth.service;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.ProfileImgFile;
import com.example.petree.domain.member.domain.SocialType;
import com.example.petree.domain.member.dto.LoginDto;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.repository.ProfileImgFileRepository;
import com.example.petree.domain.member.service.RedisService;
import com.example.petree.global.error.ErrorCode;
import com.example.petree.global.error.exception.AuthenticationException;
import com.example.petree.global.jwt.dto.JwtTokenDto;
import com.example.petree.global.jwt.service.JwtTokenProvider;
import com.example.petree.global.web.external.model.OAuthAttributes;
import com.example.petree.global.web.external.service.SocialLoginApiService;
import com.example.petree.global.web.external.service.SocialLoginApiServiceFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * packageName    : com.example.petree.global
 * fileName       : OauthLoginService
 * author         : 박수현
 * date           : 2023-06-30
 * description    : 다형성과 팩토리 패턴을 이용하여 다른 소셜 로그인을 추가하더라도 OauthLoginService는 코드 수정없이 동작할 수 있도록 구현
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-30        박수현              최초 생성
 */

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final ProfileImgFileRepository profileImgFileRepository;

    /**
     * @author 박수현
     * @date 2023-07-29
     * @description : 카카오 서버에서 받은 토큰을 기반으로 사용자 정보 조회
     * @return OauthLoginDto.Response
     */

    public LoginDto.LoginResponseDto oauthLogin(String authorizationHeader, String accessToken, SocialType socialType) throws IOException {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(socialType);
        log.info("확인" + socialLoginApiService.toString());
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
        log.info("userInfo : {}",  userInfo);

        JwtTokenDto jwtTokenDto;

        Member kakaoId = memberRepository.findByProviderId(userInfo.getEmail());

        ProfileImgFile profileImgFile = profileImgFileRepository.findByMember(kakaoId).orElse(null);


        boolean isVerified = false;
        if (kakaoId instanceof Breeder) {
            isVerified = ((Breeder) kakaoId).getIsVerified();
        } else if (kakaoId instanceof Adopter) {
            isVerified = ((Adopter) kakaoId).getIsVerified();
        }

        //최초로 카카오 계정과 연동하려하는 경우 (즉, 자체로그인을 거치고 나서 최초로 소셜로그인을 이용하는 경우)
        if(kakaoId == null && authorizationHeader != null){

            String accessTokenBytheFirstSocial = authorizationHeader.split(" ")[1];
            Member member = jwtTokenProvider.getMember(accessTokenBytheFirstSocial);
            member.setProviderId(String.valueOf(kakaoId));

            // 토큰 생성
            jwtTokenDto = jwtTokenProvider.createJwtTokenDto(member.getEmail(), member.getRole(), isVerified);
            redisService.setRefreshToken(member.getEmail(), jwtTokenDto.getRefreshToken());
            return LoginDto.LoginResponseDto.of(jwtTokenDto, profileImgFile.getFileUrl());
        }
        //기존에 카카오계정과 연동된 경우
        // - 사실 이럴수 없을듯. 왜냐면 나의 실제 카카오 계정정보가 프로젝트 DB에 저장되어있어야하는데 그럴 수가 없음.
        else if(kakaoId != null){

            // 토큰 생성
            jwtTokenDto = jwtTokenProvider.createJwtTokenDto(kakaoId.getEmail(), kakaoId.getRole(), isVerified);
            redisService.setRefreshToken(kakaoId.getEmail(), jwtTokenDto.getRefreshToken());

            return LoginDto.LoginResponseDto.of(jwtTokenDto, profileImgFile.getFileUrl());
        } else {
            throw new AuthenticationException(ErrorCode.NOT_OAUTH_HISTORY);
        }
    }
}
