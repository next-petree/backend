package com.example.petree.domain.member.service;

import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));

        return createUserDetails(member);
    }

    private UserDetails createUserDetails(Member member){

        List<GrantedAuthority> authority = List.of(new SimpleGrantedAuthority(member.getRole().getKey()));
        // System.out.println("!!!!" + authority);

        return new User(
                member.getEmail(),
                member.getPassword(),
                authority
        );
    }
}
