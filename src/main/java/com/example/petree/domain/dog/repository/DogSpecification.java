package com.example.petree.domain.dog.repository;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.dog.domain.Status;
import com.example.petree.domain.main_breed.domain.Size;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * packageName    : com.example.petree.domain.dog.repository
 * fileName       : DogSpecification
 * author         : 정세창
 * date           : 2023/07/10
 * description    : Dog에 대한 Specification 쿼리들
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/10        정세창               init
 * 2023/08/14        정세창         필터링 조건 추가 및 일부 수정
 */
public class DogSpecification {

    public static Specification<Dog> isVerified(String isVerified) {
        return (root, query, cb) -> {
            if(isVerified == null) {
                return cb.conjunction();
            } else if ("yes".equalsIgnoreCase(isVerified.trim())) {
                return cb.equal((root.get("breeder").get("isVerified")), true);
            } else if ("no".equalsIgnoreCase(isVerified.trim())) {
                return cb.equal(root.get("breeder").get("isVerified"), false);
            } else {
                return cb.conjunction();
            }
        };
    }

    public static Specification<Dog> hasDogType(Long dogTypeId) {
        return ((root, query, cb) -> {
            if (dogTypeId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("dogType").get("id"), dogTypeId);
        });
    }

    public static Specification<Dog> hasDogNameKeyword(String keyword) {
        return ((root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(root.get("name"), "%" + keyword.trim() + "%");
        });
    }

    public static Specification<Dog> hasMainBreedKeyword(String keyword) {
        return ((root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(root.get("mainBreed").get("type"), "%" + keyword.trim() + "%");
        });
    }

    /**
     * @author 이지수
     * @date 2023-08-29
     * @description : 검색 조건 추가 후 견종으로 검색하기 위해 추가
     */
    public static Specification<Dog> hasDogTypeKeyword(String keyword) {
        return ((root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(root.get("dogType").get("name"), "%" + keyword.trim() + "%");
        });
    }


    public static Specification<Dog> filterAvailableDog(Boolean isAvailable) {
        return ((root, query, cb) -> {
            if (isAvailable == null || !isAvailable) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"),Status.AVAILABLE);
        });
    }

    public static Specification<Dog> filterByGender(Gender gender) {
        return ((root, query, cb) -> {
            if (gender == null) {
                return cb.conjunction();
            }
            if (gender == Gender.MALE) {
                return cb.equal(root.get("gender"), Gender.MALE);
            } else if (gender == Gender.FEMALE) {
                return cb.equal(root.get("gender"), Gender.FEMALE);
            } else {
                return cb.conjunction();
            }
        });
    }

    public static Specification<Dog> filterBySize(Size size) {
        return ((root, query, cb) -> {
            if (size == null) {
                return cb.conjunction();
            }
            if (size == Size.EXTRA_SMALL) {
                return cb.equal(root.get("dogType").get("size"), Size.EXTRA_SMALL);
            } else if (size == Size.SMALL) {
                return cb.equal(root.get("dogType").get("size"), Size.SMALL);
            } else if (size == Size.MEDIUM){
                return cb.equal(root.get("dogType").get("size"), Size.MEDIUM);
            } else if (size == Size.LARGE){
                return cb.equal(root.get("dogType").get("size"), Size.LARGE);
            } else {
                return cb.conjunction();
            }
        });
    }

    public static Specification<Dog> filterByBreeder(Breeder breeder) {
        return ((root, query, cb) -> cb.equal(root.get("breeder"), breeder));
    }
    /**
     * @author 이지수
     * @date 2023-08-31
     * @description : 검색 조건 -> 전체 추가 (전체는 최신순으로 정렬)
     */

    public static Specification<Dog> latestCreate() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("createDate")));
            return cb.conjunction();
        };
    }
}
