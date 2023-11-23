package com.example.petree.global.web.kakao_token.controller;

import com.example.petree.domain.member.domain.SocialType;
import com.example.petree.domain.member.dto.LoginDto;
import com.example.petree.domain.member.schema.LoginSchema;
import com.example.petree.domain.member.service.RedisService;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.web.kakao_token.client.KakaoTokenClient;
import com.example.petree.global.web.kakao_token.dto.KakaoTokenDto;
import com.example.petree.global.web.oauth.service.OauthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.bouncycastle.asn1.cms.CMSAttributes.contentType;

/**
 * packageName    : com.example.petree.global.web.kakao_token
 * fileName       : KakaoTokenController
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 카카오 소셜 로그인 API
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@Component
@Tag(name = "회원관리 API", description = "회원관리 관련 API")
@RequestMapping("/api")
public class KakaoTokenController {

    private final OauthLoginService oauthLoginService;
    private final KakaoTokenClient kakaoTokenClient;
    private final Response response;

    @Value("${security.oauth2.client.registration.kakao.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.registration.kakao.clientSecret}")
    private String clientSecret;

    @Value("${security.oauth2.client.registration.kakao.redirectUri}")
    private String redirectUri;

    /**
     * @author 박수현
     * @date 2023-07-11
     * @description : 프론트에서 전달받은 인가코드로 회원정보조회
     * @return
     */

    @GetMapping("/oauth/kakao/callback")
    @Operation(summary = "카카오 로그인", description = "인가코드를 통해 회원정보 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginSchema.LoginSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> login(
            @Parameter(description = "카카오로부터 전달받은 인가코드", required = true)
            @RequestParam("code") String code,
            @Parameter(description = "Access Token, 최초로 소셜로그인을 이용하려는 사람은 AT를 요청보내야함. <br>" +
                    "소셜로그인을 이전에 한번이라도 이용한 사용자는 AT를 요청할 필요가 없음",
                    example = "Bearer AccessToken")
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) throws IOException {
        log.info("인가코드 : " + code);
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri(redirectUri)
                .build();
        log.info("kakaoTokenRequestDto : " + kakaoTokenRequestDto);
        KakaoTokenDto.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
        log.info("kakaoToken : " + kakaoToken.toString());
        log.info("카카오에서 발급해준 엑세스 토큰 : " + kakaoToken.getAccess_token());
        LoginDto.LoginResponseDto jwtTokenResponseDto = oauthLoginService.oauthLogin(authorizationHeader, kakaoToken.getAccess_token(), SocialType.KAKAO);
        return response.success(HttpStatus.OK, jwtTokenResponseDto);
    }


}
