package com.example.petree.domain.verification.repository;


import com.example.petree.domain.verification.domain.Certification;
import com.example.petree.domain.verification.domain.Status;
import com.example.petree.domain.verification.domain.VerificationRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;

/**
 * @author 이지수
 * @date 2023-08-09
 * @description : Spring JPA Specification을 사용해 유연하게 조회 API 만들기
 * @date 2023-08-10
 * @description : 이메일과 닉네임을 통한 검색은 해당 단어를 포함한 것들 모두 보이게 수정
 * @return
 */

public class RequestSpecification {

    public static Specification<VerificationRequest>breederEmail(String keyword) {
        return (root, query, criteriaBuilder)->
                criteriaBuilder.like(root.get("breeder").get("email"),"%" + keyword + "%");
    }

    public static Specification<VerificationRequest>breederNickname(String keyword) {
        return (root, query, criteriaBuilder)->
                criteriaBuilder.like(root.get("breeder").get("nickname"), "%" + keyword + "%");
    }

    public static Specification<VerificationRequest> hasSubmitDate(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.function("DATE_FORMAT", String.class, root.get("submitDate"), criteriaBuilder.literal("%Y-%m-%d")),
                        "%" + keyword + "%");
    }

    public static Specification<VerificationRequest> hasStatus(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Expression<Status> statusExpression = root.get("status");
            Status statusValue = Status.valueOf(keyword.toUpperCase()); // 열거형 값으로 변환
            return criteriaBuilder.equal(statusExpression, statusValue);
        };
    }

    public static Specification<VerificationRequest> hasCertification(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Expression<Certification> certificationExpression = root.get("certification");
            Certification certificationValue = Certification.valueOf(keyword.toUpperCase()); // 열거형 값으로 변환
            return criteriaBuilder.equal(certificationExpression, certificationValue);
        };
    }

/**
 * @author 이지수
 * @date 2023-08-31
 * @description : 최신순 정렬
 */

    public static Specification<VerificationRequest> latestCreate() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("submitDate")));
            return cb.conjunction();
        };
    }

}
