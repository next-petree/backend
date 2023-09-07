package com.example.petree.domain.matching.service;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.Status;
import com.example.petree.domain.dog.repository.DogRepository;
import com.example.petree.domain.matching.domain.Matching;
import com.example.petree.domain.matching.domain.MatchingApproval;
import com.example.petree.domain.matching.domain.Pledge;
import com.example.petree.domain.matching.dto.*;
import com.example.petree.domain.matching.repository.*;
import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import com.example.petree.domain.notification.domain.Notification;
import com.example.petree.domain.notification.repository.NotificationRepository;
import com.example.petree.global.Response;
import com.example.petree.global.util.FileUtil;
import com.example.petree.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.example.petree.domain.matching.service
 * fileName       : MatchingService
 * author         : jsc
 * date           : 2023/07/09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/09        jsc
 */
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final BreederRepository breederRepository;
    private final DogRepository dogRepository;
    private final MatchingApprovalRepository matchingApprovalRepository;
    private final NotificationRepository notificationRepository;
    private final S3Util s3Util;
    private final FileUtil fileUtil;
    private final Response response;

    /**
     * 분양희망자가 분양 신청을 진행하는 서비스 메소드
     * @param adopter 분양 신청을 하는 분양 희망자 엔티티
     * @param matchingFormDto 분양 희망자가 제출한 분양 신청서 dto
     * @return
     */
    @Transactional
    public ResponseEntity<?> addMatching(Adopter adopter, MatchingFormDto matchingFormDto) {

        // matchingFormDto에서 신청 대상인 강아지와 브리더 가져오기
        Breeder breeder = breederRepository.findById(matchingFormDto.getBreederId()).orElse(null);
        Dog dog = dogRepository.findById(matchingFormDto.getDogId()).orElse(null);
        if(breeder == null) return response.error("해당 breederId를 식별자로 가지는 브리더가 존재하지 않습니다.");
        if(dog == null) return response.error("해당 dogId를 식별자로 가지는 강아지가 존재하지 않습니다.");

        // 새 분양 신청 엔티티와 마음가짐 엔티티 생성 및 엔티티 간 연결
        Matching matching = new Matching(LocalDate.now(), dog, adopter, breeder);
        Pledge pledge = new Pledge(matchingFormDto.getPledgeContent1(), matchingFormDto.getPledgeContent2());
        matching.setPledge(pledge);

        // 변경 사항 커밋 및 결과 반환
        matchingRepository.save(matching);
        return response.success(HttpStatus.CREATED);
    }


    /**
     * 브리더의 분양 신청 리스트(수신)를 가져오는 서비스 메소드
     * @param pageable 컨트롤러에서 받은 Default Pageable 객체
     * @param breeder 대상 브리더 엔티티
     * @param keyword 검색어(이메일 or 닉네임) 키워드
     * @return
     */
    public ResponseEntity<?> getMatchingsOfBreeder(Pageable pageable, Member breeder, String keyword) {

        // Matching 엔티티 중 현재 브리더와 연관되었으며, 검색 키워드(있다면)가 이메일이나 닉네임에 포함되는 엔티티만 조회하는 specification 생성
        Specification<Matching> spec = Specification
                .where(MatchingSpecification.filterByMember(breeder, Role.BREEDER))
                .and(MatchingSpecification.hasEmailOrNickname(keyword, Role.BREEDER));

        // 위에서 만든 specification을 이용하여 조건을 만족하는 Matcing 엔티티 조회
        Page<Matching> matchings = matchingRepository.findAll(spec, pageable);

        // 조회 결과를 dto로 가공한 뒤 결과 반환
        return response.success(
                HttpStatus.OK,
                matchings.map(SimpleMatchingOfBreederDto::new));

    }

    /**
     * 분양 희망자의 분양 신청 리스트(수신)를 가져오는 서비스 메소드
     * @param pageable 컨트롤러에서 받은 Default Pageable 객체
     * @param adopter 대상 브리더 엔티티
     * @param keyword 검색어(이메일 or 닉네임) 키워드
     * @return
     */
    public ResponseEntity<?> getMatchingsOfAdopter(Pageable pageable, Member adopter, String keyword) {

        // Matching 엔티티 중 현재 분양 희망자와 연관되었으며, 검색 키워드(있다면)가 이메일이나 닉네임에 포함되는 엔티티만 조회하는 specification 생성
        Specification<Matching> spec = Specification
                .where(MatchingSpecification.filterByMember(adopter, Role.ADOPTER))
                .and(MatchingSpecification.hasEmailOrNickname(keyword, Role.ADOPTER));

        // 위에서 만든 specification을 이용하여 조건을 만족하는 Matcing 엔티티 조회
        Page<Matching> matchings = matchingRepository.findAll(spec, pageable);

        // 조회 결과를 dto로 가공한 뒤 결과 반환
        return response.success(
                HttpStatus.OK,
                matchings.map(SimpleMatchingOfAdopterDto::new));

    }


    /**
     * 브리더가 특정 분양 신청 정보를 상세조회하는 서비스 메소드
     * @param matchingId 분양 신청 식별자 id
     * @return
     */
    public ResponseEntity<?> getMatchingOfBreeder(Long matchingId) {

        // matchingId에 해당하는 Matching 엔티티 가져오기
        Matching matching = matchingRepository.findById(matchingId).orElse(null);
        if (matching == null) {
           return response.error("해당 matchingId를 식별자로 가지는 매칭 엔티티가 존재하지 않습니다.");
        }

        // dto로 가공하여 결과 반환
        return response.success(
                HttpStatus.OK,
                new DetailMatchingOfBreederDto(matching)
        );
    }

    /**
     * 분양 희망자가 특정 분양 신청 정보를 상세조회하는 서비스 메소드
     * @param matchingId 분양 신청 식별자 id
     * @return
     */
    public ResponseEntity<?> getMatchingOfAdopter(Long matchingId) {

        // matchingId에 해당하는 Matching 엔티티 가져오기
        Matching matching = matchingRepository.findById(matchingId).orElse(null);
        if (matching == null) {
            return response.error("해당 matchingId를 식별자로 가지는 매칭 엔티티가 존재하지 않습니다.");
        }

        // dto로 가공하여 결과 반환
        return response.success(
                HttpStatus.OK,
                new DetailMatchingOfAdopterDto(matching)
        );
    }

    /**
     * 브리더가 수신한 매칭 신청을 승인/거부 처리할 때 사용되는 서비스 메소드
     * @param matchingId 분양 신청 식별자 id
     * @param isApproved 승인/미승인 여부
     * @return
     */
    @Transactional
    public ResponseEntity<?> processMatching(Long matchingId, Boolean isApproved) {
        // 분양 신청 승인 엔티티 생성
        MatchingApproval matchingApproval = new MatchingApproval(isApproved);

        // matchingId에 해당하는 Matching 엔티티 가져오기
        Matching matching = matchingRepository.findById(matchingId).orElse(null);
        if (matching == null) return response.error("matchingId를 식별자로 가지는 매칭 엔티티가 존재하지 않습니다.");

        // 각 엔티티 연결 및 수정 사항 반영
        matching.setMatchingApproval(matchingApproval);
        matchingRepository.save(matching);
        matchingApprovalRepository.save(matchingApproval);

//        String notificationContent = (isApproved)
//                ? String.format("브리더(%s)가 입양 희망 요청을 수락하셨습니다.", matching.getBreeder().getNickname())
//                : String.format("브리더(%s)가 입양 희망 요청을 거절하였습니다.", matching.getBreeder().getNickname());
//        Notification notification = new Notification(notificationContent, LocalDateTime.now(), matching.getAdopter());
//        notificationRepository.save(notification);

        return response.success(HttpStatus.OK);
    }
}
