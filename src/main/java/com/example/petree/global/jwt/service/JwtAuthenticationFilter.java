package com.example.petree.global.jwt.service;

import com.example.petree.domain.member.domain.Role;
import com.example.petree.domain.member.service.CustomUserDetailService;
import com.example.petree.global.Response;
import com.example.petree.global.jwt.dto.JwtTokenDto;
import com.example.petree.global.util.AuthorizationHeaderUtils;
import com.mysema.commons.lang.Pair;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : jwt 자체로그인으로, 클라이언트가 AT를 요청시 AT기반으로 로그인,
     *                                 클라이언트가 RT를 요청시 RT를 기반으로 AT 새로 발급 후 로그인
     * @return void
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.getAccessToken(request);

        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken)) {
                // 유효한 Access Token (AT)을 사용하여 API 요청 처리
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(accessToken));
            } else {
                // 만료된 Access Token (AT)을 사용하거나 유효하지 않은 경우,
                // Refresh Token (RT)을 사용하여 새로운 Access Token 발급 및 API 요청 처리
                String refreshToken = jwtTokenProvider.getRefreshToken(accessToken);
                if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                    String email = jwtTokenProvider.getUserEmail(refreshToken);
                    Pair<Role, Boolean> pair = jwtTokenProvider.getRoleAndVerification(email);
                    log.info("인증정보 : " + pair.getSecond());
                    JwtTokenDto jwtTokenDto  = jwtTokenProvider.createJwtTokenDto(email, pair.getFirst(), pair.getSecond());

                    // 새로운 Access Token을 헤더에 추가
                    jwtTokenProvider.setHeaderAccessToken(response, jwtTokenDto.getAccessToken());

                    this.setAuthentication(jwtTokenDto.getAccessToken());
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "RefreshToken이 만료되었습니다.");
                }
            }
        }

        filterChain.doFilter(request, response);
    }



    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : AT를 기반으로 SecurityContext에 Authentication 객체를 저장한다면,
     *                Principal기반의 사용자 정보를 다른 외부 컨트롤러에서 사용할 수 있음.
     * @return void
     */

    public void setAuthentication(String accessToken) {
        // 토큰으로부터 유저 정보를 받아오기.
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        // SecurityContext 에 Authentication 객체를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
