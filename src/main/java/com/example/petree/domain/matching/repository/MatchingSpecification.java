package com.example.petree.domain.matching.repository;

import com.example.petree.domain.matching.domain.Matching;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

/**
 * packageName    : com.example.petree.domain.matching.repository
 * fileName       : MatchingSpecification
 * author         : 정세창
 * date           : 2023/07/11
 * description    : Matching에 대한 specification 쿼리들
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/11        정세창               init
 */
public class MatchingSpecification {

    public static Specification<Matching> filterByMember(Member member, Role role) {
        String roleStr = (role == Role.ADOPTER) ? "adopter" : "breeder";
        return ((root, query, cb) -> cb.equal(root.get(roleStr), member)
        );
    }
    public static Specification<Matching> hasEmailOrNickname(String keyword, Role role) {
        String target = (role == Role.ADOPTER) ? "breeder" : "adopter";
        return ((root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            Predicate predicateEmail = cb.like(root.get(target).get("email"), "%" + keyword.trim() + "%");
            Predicate predicateNickname = cb.like(root.get(target).get("nickname"), "%" + keyword.trim() + "%");
            return cb.or(predicateNickname, predicateEmail);
        });
    }

}
