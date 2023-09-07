package com.example.petree.global.web.external.model;

import com.example.petree.domain.member.domain.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : com.example.petree.global.web.external.model
 * fileName       : OauthAttributes
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 소셜 플랫폼마다 반환하는 회원정보 형태가 다름.
 *                  OAuthAttributes형태로 통일.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@ToString
@Getter @Builder
public class OAuthAttributes {

    private String nickname;
    private String email;
    private String profile;
    private SocialType socialType;

}
