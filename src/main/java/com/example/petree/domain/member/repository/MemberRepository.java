package com.example.petree.domain.member.repository;

import com.example.petree.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : com.example.petree.global
 * fileName       : MemberRepository
 * author         : qkrtn_ulqpbq2
 * date           : 2023-06-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-30        qkrtn_ulqpbq2       최초 생성
 */

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    /**
     * @author 박수현
     * @date 2023-07-29
     * @description : Oauth인증을 받았는지 조회
     * @return
     */

    Member findByProviderId(String providerId);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Member findByEmailAndPhoneNumber(String email, String phoneNumber);

}

