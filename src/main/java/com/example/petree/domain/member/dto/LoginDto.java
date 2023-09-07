package com.example.petree.domain.member.dto;

import com.example.petree.global.ResponseSchema;
import com.example.petree.global.jwt.dto.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * packageName    : com.example.petree.domain.member.dto
 * fileName       : LoginDto
 * author         : 박수현
 * date           : 2023-07-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-28        박수현              최초 생성
 */

@Getter
@EqualsAndHashCode(callSuper = true)
public class LoginDto extends ResponseSchema {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class LoginRequestDto{
        @Schema(description = "이메일", example = "test1@test.com", required = true)
        private String email;
        @Schema(description = "비밀번호", example = "1234@", required = true)
        private String password;
    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto{
        @Schema(description = "토큰 기반 인증 방식", example = "Bearer", required = true)
        private String grantType;

        @Schema(description = "AccessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2NTg0ODAyOTYsImV4cCI6MTY1ODQ4MTE5NiwibWVtYmVySWQiOjEsInJvbGUiOiJBRE1JTiJ9.qr5fOs9NIO5UYJzqgisESOXorASLphj04uyjF1Breolj4cou_k6py0egF8f3OxWjQXps3on7Ko3jwIaL_2voRg", required = true)
        private String accessToken;

        @Schema(description = "AccessToken 만료 시간", example = "2022-07-22 18:13:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        @Schema(description = "RefreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0aASDgwMjk3LCJleHAiOjE2NTk2ODk4OTYsIm1lbWJlcklkIjoxfQ.hxgq_DIU554lUnUCSAGTYOiaXLXwgpyIM2h8a5de3ALEOY-IokElS6VbMmVTKlpRaLfEzzcr3FkUDrNisRt-bA", required = true)
        private String refreshToken;

        @Schema(description = "RefreshToken 만료 시간", example = "2022-08-05 18:13:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;

        @Schema(description = "프로필 사진 저장 경로", example = "{프로필 사진 저장 경로}")
        private String profileImgUrl;

        public static LoginDto.LoginResponseDto of(JwtTokenDto jwtTokenDto, String profileImgUrl) {
            return LoginResponseDto.builder()
                    .grantType(jwtTokenDto.getGrantType())
                    .accessToken(jwtTokenDto.getAccessToken())
                    .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
                    .refreshToken(jwtTokenDto.getRefreshToken())
                    .refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
                    .profileImgUrl(profileImgUrl)
                    .build();
        }
    }
}
