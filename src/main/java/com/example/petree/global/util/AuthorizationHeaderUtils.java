package com.example.petree.global.util;

import com.example.petree.domain.member.service.RedisService;
import com.example.petree.global.error.ErrorCode;
import com.example.petree.global.error.exception.AuthenticationException;
import com.example.petree.global.jwt.constant.GrantType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class AuthorizationHeaderUtils {

    private static final RedisService redisService = null;

    public static void validateAuthorization(String authorizationHeader) {

        if(authorizationHeader != null){
            String[] authorizations = authorizationHeader.split(" ");
            if(authorizations.length < 2 || (!GrantType.BEARER.getType().equals(authorizations[0]))) {
                throw new AuthenticationException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
            }

            // 3. AT가 Redis의 블랙리스트다면 BLACKLISTED_ACCESS_TOKEN 반환
            try {
                if (redisService.isTokenInBlacklist(authorizations[1])) {
                    throw new AuthenticationException(ErrorCode.BLACKLISTED_ACCESS_TOKEN);
                }
            } catch (Exception e) {
                // 예외 처리 로직 추가
                new RuntimeException("Failed to check token blacklist", e);
            }
        }
    }

}
