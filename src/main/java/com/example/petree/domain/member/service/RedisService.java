package com.example.petree.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    /**
     * @author 박수현
     * @date 2023-08-10
     * @description : 인증코드 Redis에 저장
     * @return
     */

    public void setVerificationCode(String phoneNumber, String code, Instant expirationTime) {
        String key = "smsVerification:" + phoneNumber;
        log.info("key : " + key);
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, code, Duration.between(Instant.now(), expirationTime));
    }

    public String getVerificationCode(String phoneNumber) {
        String key = "smsVerification:" + phoneNumber;
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void setRefreshToken(String email, String refreshToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, refreshToken, Duration.ofDays(7));
    }

    public String getRefreshToken(String email) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(email);
    }

    /**
     * @author 박수현
     * @date 2023-07-11
     * @description : RT 삭제
     * @return
     */
    public void delValues(String refreshToken) {
        redisTemplate.delete(refreshToken.substring(7));
    }

    /**
     * @author 박수현
     * @date 2023-07-11
     * @description : 로그아웃 시 블랙리스트에 AT저장
     * @return
     */
    public void addToBlacklist(String accessToken) {
        String key = "token:blacklist";
        redisTemplate.opsForSet().add(key, accessToken);
    }

    /**
     * @author 박수현
     * @date 2023-07-11
     * @description : AT가 Redis의 블랙리스트에 저장되어있는지 확인, opsForSet을 통해 SET데이터에 접근하여 ACCESSTOKEN이 있는지 확인
     * @return
     */
    public boolean isTokenInBlacklist(String accessToken) {
        try {
            String key = "token:blacklist";
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, accessToken));
        } catch (Exception e) {
            throw new RuntimeException("Failed to check token blacklist", e);
        }
    }

}
