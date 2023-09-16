package com.example.petree.domain.verification.service;


import com.example.petree.domain.member.domain.Admin;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.dto.ProfileDto;
import com.example.petree.domain.member.repository.AdminRepository;
import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.verification.domain.*;
import com.example.petree.domain.verification.dto.VerificationFormDto;
import com.example.petree.domain.verification.dto.VerificationFromResponseDto;
import com.example.petree.domain.verification.dto.VerificationListDto;
import com.example.petree.domain.verification.repository.RequestSpecification;
import com.example.petree.domain.verification.repository.VerificationApprovalRepository;
import com.example.petree.domain.verification.repository.VerificationRequestRepository;
import com.example.petree.global.Response;
import com.example.petree.global.util.FileUtil;
import com.example.petree.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.petree.domain.member.dto.ProfileDto.createProfileImgResponseDTO;

/**
 * packageName    : com.example.petree.domain.verification.service
 * fileName       : VerificationService
 * author         : 이지수
 * date           : 2023/08/07
 * description    : 브리더 인증에 관한 서비스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-07        이지수          최초생성
 * 2023-08-08        이지수          승인상태, 브리더 인증 여부 판단 추가
 * 2023-08-08        이지수          브리더 자격증 종류 추가
 * 2023-08-09        이지수          검색, 페이징 추가
 * *수정필요: 검색 조건 아무 것도 선택 안했을 때 아무 정보도 안뜸
 *          이메일,닉네임으로 검색할 때, 전체로 비교하지말고
 *          키워드로 하는 것 추가하기
 * 2023-08-09         이지수          최종수정
 * 2023-08-10         이지수          브리더 요청 리스트 확인 시 첨부파일 목록 추가
 *
 */

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRequestRepository verificationRequestRepository;
    private final VerificationApprovalRepository verificationApprovalRepository;
    private RequestSpecification requestSpecification;
    private final MemberRepository memberRepository;
    private final BreederRepository breederRepository;
    private final S3Util s3Util;
    private final FileUtil fileUtil;
    private final Response response;

    public boolean isAlreadyUploaded(Breeder breeder, Certification certification) {
        VerificationRequest existingRequest = verificationRequestRepository.findByBreederAndCertification(breeder, certification);
        return existingRequest != null;
    }

    /**
     * @author 이지수
     * @date 2023-08-07
     * @description : 브리더 인증 승인 상태 enum 추가 , WAITING으로 생성됨
     * @return status
     * @date 2023-08-09
     * @description : 브리더는 자신이 제출하고 하는 자격증의 종류를 선택할 수 있다
     * @return certification
     */
// 브리더가 관리자에게 자료를 첨부하여 인증 요청을 보내는 메소드
    @Transactional
    public VerificationFromResponseDto addVerification(Breeder breeder,VerificationFormDto verificationFormDto, Admin admin) {

        // 새로운 요청을 생성한다.
        VerificationRequest verificationRequest = new VerificationRequest(LocalDate.now(),breeder);
        // 처리 상태는 Watting으로 자동으로 저장한다.
        verificationRequest.setStatus(Status.WAITING);

        // 파일을 여러 개 첨부한다
        MultipartFile multipartFile = verificationFormDto.getVerificationFiles();
        String originalName = multipartFile.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalName);
        String res = s3Util.upload(multipartFile, "verification-file", filename);
        AttachedFile verificationFiles = new AttachedFile(originalName, filename, res, verificationRequest);
        verificationRequest.addAttachedFile(verificationFiles);
        verificationRequest.setCertification(verificationFormDto.getCertification());

        // 관리자를 할당합니다.
        verificationRequest.setAdmin(admin);
        verificationRequestRepository.save(verificationRequest);

        return VerificationFromResponseDto.createVerificationFromResponseDto(verificationRequest);
    }

    /**
     * @author 이지수
     * @date 2023-08-08
     * @description : 승인 여부에 따라 브리더의 인증여부 변경 -> 1개 이상의 승인이 있으면 인증된 브리더
     * 브리더 인증 승인 상태 추가 : WAITING에서 APPROVAL또는 REFUSAL로 변경됨
     */
    // 관리자가 브리더의 요청을 처리하는 메소드
    @Transactional
    public ResponseEntity<?> processVerification(Long verificationRequestId, Long breederId, Boolean isApproved) {
        VerificationApproval verificationApproval = new VerificationApproval(isApproved);

        VerificationRequest verificationRequest = verificationRequestRepository.findById(verificationRequestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 요청이 존재하지 않습니다."));

        // 인증 승인 상태를 처리한다.
        verificationRequest.setVerificationApproval(verificationApproval);
        Status requestStatus = (isApproved)
                ? Status.APPROVAL
                : Status.REFUSAL;
        verificationRequest.setStatus(requestStatus);
        verificationApprovalRepository.save(verificationApproval);

        // 승인 상태에 따라 브리더의 인증 여부를 변경한다.
        Boolean breederIsVerified = (isApproved)
                ? Boolean.TRUE
                : Boolean.FALSE;
        Breeder breeder = breederRepository.findById(breederId).orElse(null);
        breeder.setIsVerified(breederIsVerified);
        List<VerificationRequest> approvedVerificationRequests = verificationRequestRepository.findByBreederIdAndStatus(breederId, Status.APPROVAL);
        if (!approvedVerificationRequests.isEmpty()) {
            breeder.setIsVerified(Boolean.TRUE);
        }
        breederRepository.save(breeder);

        return response.success(HttpStatus.OK);
    }

    /**
     * @author 이지수
     * @date 2023-08-09
     * @description : 검색(브리더 이메일, 닉네임, 인증 요청 날짜, 자격증 종류, 브리더 인증 진행 상태에 따라)
     * @date 2023-08-10
     * @description : 브리더 이메일과 닉네임으로 검색할 경우 검색한 단어를 포함한 이메일이나 닉네임을 가진 경우 모두 반환되게 수정
     * @description : 아무런 검색 조건을 넣지 않은 경우 모든 레코드 반환
     * @description : 반환 값에 파일 리스트 추가
     */
    @Transactional(readOnly = true)
    public ResponseEntity<?> getVerificationRequests(
            String searchType,String keyWord ,Pageable pageable) {
        Specification<VerificationRequest> spec = Specification.where(null);

        try {
            if (SearchType.EMAIL.getSearchType().equals(searchType)) {
                spec = spec.and(RequestSpecification.breederEmail(keyWord));
            } else if (SearchType.NICKNAME.getSearchType().equals(searchType)) {
                spec = spec.and(RequestSpecification.breederNickname(keyWord));
            } else if (SearchType.CERTIFICATION.getSearchType().equals(searchType)) {
                spec = spec.and(RequestSpecification.hasCertification(keyWord));
            } else if (SearchType.STATUS.getSearchType().equals(searchType)) {
                spec = spec.and(RequestSpecification.hasStatus(keyWord));
            } else if (SearchType.SUBMITDATE.getSearchType().equals(searchType)) {
                spec = spec.and(RequestSpecification.hasSubmitDate(keyWord));
            } else if (SearchType.WHOLE.getSearchType().equals(searchType)) {
                spec = spec.and(RequestSpecification.latestCreate());
            } else {
                throw new IllegalArgumentException("유효하지 않은 검색 타입입니다.");
            }
        }catch (IllegalArgumentException ex) {
            return response.fail(HttpStatus.BAD_REQUEST, "올바르지 않은 검색 타입입니다.");
        }


        Page<VerificationRequest> result = verificationRequestRepository.findAll(spec, pageable);

        return response.success(HttpStatus.OK, new PageImpl<>(result.getContent().stream()
                .map(verificationRequest -> new VerificationListDto(
                        verificationRequest.getCertification(),
                        verificationRequest.getStatus(),
                        verificationRequest.getBreeder().getEmail(),
                        verificationRequest.getBreeder().getNickname(),
                        verificationRequest.getSubmitDate(),
                        verificationRequest.getVerificationFiles()
                                .stream()
                                .map(AttachedFile::getFileUrl)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList()), pageable, result.getTotalElements()));
    }
}

