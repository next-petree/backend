package com.example.petree.global.config;

import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * packageName    : com.example.petree.global
     * fileName       : UsernamePwdAuthenticationProvider
     * author         : 박수현
     * date           : 2023-07-02
     * description    : 자체 로그인 -> jwt로 향후 개선할 것.
     * ===========================================================
     * DATE              AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2023-06-30        박수현              최초 생성
     */

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member != null) {
            if (passwordEncoder.matches(pwd, member.getPassword())) {
                return new UsernamePasswordAuthenticationToken(email, pwd);
            } else {
                throw new BadCredentialsException("비밀번호를 잘못 입력하셨습니다.");
            }
        }else {
            throw new BadCredentialsException("등록되지 않은 사용자입니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
