package com.example.petree.global.jwt.service;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.service.CustomUserDetailService;
import com.example.petree.domain.member.service.RedisService;
import com.example.petree.global.jwt.constant.GrantType;
import com.example.petree.global.jwt.constant.TokenType;
import com.example.petree.global.jwt.dto.JwtTokenDto;
import com.example.petree.global.util.AuthorizationHeaderUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.commons.lang.Pair;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailService customUserDetailService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;

    private final long accessTokenExpirationTime = 7 * 24 * 60 * 60 * 1000; // 1주로 설정
    private final long refreshTokenExpirationTime = ChronoUnit.DAYS.getDuration().multipliedBy(30).toMillis(); //30 * 24 * 60 * 60 * 1000; //한달
    private SecretKey tokenSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 이메일과 Role기반으로 AT, RT 생성
     * @return
     */

    public JwtTokenDto createJwtTokenDto(String email, Role role, Boolean verification) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(email, role, verification, accessTokenExpireTime);
        String refreshToken = createRefreshToken(email, refreshTokenExpireTime);
        return JwtTokenDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : AT의 만료시간 생성
     * @return Date
     */

    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + accessTokenExpirationTime);
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : RT의 만료시간 생성
     * @return Date
     */

    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + refreshTokenExpirationTime);
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : AT생성
     * @return String
     */

    public String createAccessToken(String email, Role role, Boolean verification ,Date expirationTime) {

        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name())    // 토큰 제목
                .setIssuedAt(new Date())                // 토큰 발급 시간
                .setExpiration(expirationTime)          // 토큰 만료 시간
                .claim("email", email)                  // 회원 이메일
                .claim("role", role.name())                // 유저 role
                .claim("verification", verification)    // 인증 정보 추가
                .signWith(tokenSecret)
                .setHeaderParam("typ", "JWT")
                .compact();

    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : RT생성
     * @return String
     */

    public String createRefreshToken(String email, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name())   // 토큰 제목
                .setIssuedAt(new Date())                // 토큰 발급 시간
                .setExpiration(expirationTime)          // 토큰 만료 시간
                .claim("email", email)      // 회원 이메일
                .signWith(tokenSecret)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 주어진 AT 혹은 RT를 검증하여 유효한 토큰인지 확인하는 역할
     * @return String
     */

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("token 만료", e);
            return false; // AccessToken이 만료된 경우
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);
            return false;
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description :  JWT 토큰은 헤더, 페이로드, 서명 세 부분으로 구성되며, 페이로드 부분에는 클레임(Claims) 정보가 있음. 즉, 사용자 정보를 반환
     * @return
     */

    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            claims = (Claims) Jwts.parser().setSigningKey(tokenSecret)
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            log.info("토큰 만료", e);
            return null;
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);
            return null; // 유효하지 않은 토큰인 경우
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : HttpServletRequest를 기반으로 클라이언트가 AT가 만료되면 RT로도 요청하므로, AT OR RT조회
     * @return String
     */

    public String getAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);
            String accessToken = authorizationHeader.split(" ")[1];
            return accessToken;
        } else {
            return null;
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : AT를 기반으로 RT조회,
     *                Redis에서는 Key가 이메일, Value가 RT로 값이 저장됨.
     *                따라서, AT를 기반으로 -> 이메일 조회 -> RT반환
     * @return
     */

    public String getRefreshToken(String accessToken) {
        if (accessToken != null) {
            Claims claims = getTokenClaims(accessToken);
            String email = claims.get("email", String.class);
            String refreshToken = redisService.getRefreshToken(email);
            return refreshToken;
        }
        return null;
    }



    /**
     * @author 박수현
     * @date 2023-07-13
     * @description : AT를 기반으로 Member반환
     * @return Member
     */
    public Member getMember(String accessToken) throws IOException {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(accessToken)
                .getBody();

        String email = claims.get("email", String.class);
        String roleJson = claims.get("role", String.class);
        log.info("roleJosn : " + roleJson);
        //Role role = objectMapper.readValue(roleJson, Role.class);
        Role role = Role.valueOf(roleJson);

        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent() && memberOptional.get().getRole() == role) {
            return memberOptional.get();
        }

        return null;
    }


    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : AT를 기반으로 Authentication객체 반환
     * @return Authentication
     */

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserEmail(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : RT를 기반으로 이메일 조회
     * @return String
     */
    public String getUserEmail(String refreshToken) {
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(refreshToken).getBody();
        return claims.get("email", String.class);
    }


    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 이메일을 기반으로 역할 조회
     * @return
     */
    public Pair<Role, Boolean> getRoleAndVerification(String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member != null) {
            Role role = member.getRole();
            boolean isVerified = false;

            if (member instanceof Breeder) {
                isVerified = ((Breeder) member).getIsVerified();
            } else if (member instanceof Adopter) {
                isVerified = ((Adopter) member).getIsVerified();
            }

            log.info("인증정보 : " + isVerified);

            return new Pair<>(role, isVerified);
        }

        return null; // 해당 이메일에 대한 정보가 없을 경우
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : AccessToken을 헤더에 저장
     * @return void
     */

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", GrantType.BEARER.getType() + " " + accessToken);
    }
}
