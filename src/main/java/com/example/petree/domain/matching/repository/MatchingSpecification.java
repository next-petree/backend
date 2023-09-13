package com.example.petree.domain.matching.repository;

import com.example.petree.domain.matching.domain.Matching;
import com.example.petree.domain.matching.domain.Search;
import com.example.petree.domain.matching.domain.SearchType;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

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
@Slf4j
public class MatchingSpecification {

    public static Specification<Matching> filterByMember(Member member, Role role) {
        String roleStr = (role == Role.ADOPTER) ? "adopter" : "breeder";
        return ((root, query, cb) -> cb.equal(root.get(roleStr), member)
        );
    }
    /*public static Specification<Matching> hasEmailOrNickname(String keyword, Role role) {
        String target = (role == Role.ADOPTER) ? "breeder" : "adopter";
        return ((root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            Predicate predicateEmail = cb.like(root.get(target).get("email"), "%" + keyword.trim() + "%");
            Predicate predicateNickname = cb.like(root.get(target).get("nickname"), "%" + keyword.trim() + "%");
            return cb.or(predicateNickname, predicateEmail);
        });
    }*/

    /*public static Specification<Matching> hasDogName(String keyword, Role role) {
        String target = (role == Role.ADOPTER) ? "breeder" : "adopter";
        return ((root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            Predicate predicateDogName = cb.like(root.get(target).get("dog").get("name"), "%" + keyword.trim() + "%");

            return predicateDogName;
        });
    }

    public static Specification<Matching> hasDogType(String keyword, Role role) {
        String target = (role == Role.ADOPTER) ? "breeder" : "adopter";
        return ((root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            Predicate predicateDogType = cb.like(root.get(target).get("dog").get("dogType").get("name"), "%" + keyword.trim() + "%");

            return predicateDogType;
        });
    }*/
    public static Specification<Matching> hasSearchFilter(Search search, Role role) {
        String target = (role == Role.ADOPTER) ? "breeder" : "adopter";
        return (root, query, cb) -> {
            if (search.isEmpty()) {
                return cb.conjunction(); // 검색 조건이 비어있으면 모든 것을 포함
            }

            List<Predicate> predicates = new ArrayList<>();

            // 검색 유형을 열거형으로 변환
            String searchType = search.getSearchType().toString().toLowerCase();
            log.info("searchType : " + searchType);
            log.info("searchKeword : " + search.getKeyword());

            if ("name".equals(searchType)) {
                predicates.add(cb.like(root.get("dog").get("name"), "%" + search.getKeyword() + "%"));
            } else if ("type".equals(searchType)) {
                predicates.add(cb.like(root.get("dog").get("dogType").get("name"), "%" + search.getKeyword() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
