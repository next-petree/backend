package com.example.petree.global.web.oauth.vaildator;

import com.example.petree.domain.member.domain.SocialType;
import com.example.petree.global.error.ErrorCode;
import com.example.petree.global.error.exception.BusinessException;

/**
 * packageName    : com.example.petree.global.web.oauth.vaildator
 * fileName       : OauthValidator
 * author         : 박수현
 * date           : 2023-07-11
 * description    : socialType의 유효성검증
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */
public class OauthValidator {

    public void validateMemberType(String socialType) {
        if(!SocialType.isSocialType(socialType)) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_TYPE);
        }
    }
}
