package com.example.petree.global.web.kakao_token.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : com.example.petree.global.web.kakao_token.dto
 * fileName       : KakaoTokenDto
 * author         : qkrtn_ulqpbq2
 * date           : 2023-07-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        qkrtn_ulqpbq2       최초 생성
 */
  
public class KakaoTokenDto {

    @Builder
    @Getter
    public static class Request {
        private String grant_type;
        private String client_id;
        private String redirect_uri;
        private String code;
        private String client_secret;
    }

    @ToString
    @Builder
    @Getter
    public static class Response {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private Integer refresh_token_expires_in;
        private String scope;
    }
}
