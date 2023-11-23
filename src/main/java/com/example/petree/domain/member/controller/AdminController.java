package com.example.petree.domain.member.controller;

import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.verification.domain.Certification;
import com.example.petree.domain.verification.domain.SearchType;
import com.example.petree.domain.verification.domain.Status;
import com.example.petree.domain.verification.domain.VerificationRequest;
import com.example.petree.domain.verification.schema.VerificationListDtoSchema;
import com.example.petree.domain.verification.schema.VerificationSchema;
import com.example.petree.domain.verification.service.VerificationService;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.exception.MissingPrincipalException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.member.controller
 * fileName       : AdminController
 * author         : 박수현
 * date           : 2023-08-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-21        박수현              최초 생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="관리자 승인하기")
@RequestMapping("/admin/verifications")
public class AdminController {

    private final VerificationService verificationService;
    private final BreederRepository breederRepository;
    private final MemberRepository memberRepository;
    private final Response response;

    @PostMapping(value = "/{verificationId}/approval")
    @Operation(
            summary = "브리더 요청에 대한 처리",
            description = "브리더 요청에 대한 처리를 한다. 승인 또는 거절을 할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브리더 인증 처리 완료",
                    content = @Content(schema = @Schema(implementation = VerificationSchema.Verification200.class))),
            @ApiResponse(responseCode = "400", description = "브리더 인증 처리에 대한 권한이 없습니다. 관리자만 브리더 인증 처리가 가능합니다. 관리자라면 로그인해주세요.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class)))
    })
    public ResponseEntity<?> processVerification(
            @Parameter(description = "브리더 인증 요청 id", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable Long verificationId,
            @Parameter(description = "요청 보낸 브리더 id", example = "1", required = true)
            @RequestParam Long breederId,
            @Parameter(description = "브리더 인증 요청에 대한 승인 or 거절", example = "true", required = true)
            @RequestParam Boolean isApproval, Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }
        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        if(member != null){
            if(member.getRole().getTitle().equals("ADMIN")){

                return verificationService.processVerification(verificationId, breederId, isApproval);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "관리자 회원이 아닙니다."));
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, Map.of("message", "로그인이 필요합니다."));
        }
    }

    /**
     * @author 이지수
     * @date 2023-08-09
     * @description : 관리자는 검색을 통해서 원하는 요청을 찾을 수 있다.
     * 검색 조건들 중 하나만 선택해서 검색해야한다. (브리더 이메일, 닉네임, 인증 요청 날짜, 자격증 종류, 브리더 인증 진행 상태)
     */
    @GetMapping
    @Operation(
            summary = "인증 요청 리스트",
            description = "브리더 인증 요청을 모아볼 수 있다." +
                    " 검색 조건에는 이메일, 닉네임, 제출일, 자격증,상태가 있다." +
                    " 이 중 하나만 골라서 검색할 수 있다."+
                    " 제출일의 검색 키워드는 2023-08-31의 형식이어야한다."+
                    " 자격증은 반드시 반려동물종합관리사/반려동물행동교정사로 검색해야한다."+
                    " 상태는 반드시 WAITING/APPROVAL/REFUSAL 으로 검색해야한다."+
                    "전체는 검색어를 빈칸으로 둔다(검색어 X)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브리더 인증 요청들 조회 성공",
                    content = @Content(schema = @Schema(implementation = VerificationListDtoSchema.VerificationListDto200.class))),
            @ApiResponse(responseCode = "400", description = "조회할 수 있는 권한이 없습니다. 관리자만 조회가 가능합니다. 관리자라면 로그인 해주세요",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class)))
    })
    public ResponseEntity<?> getVerificationRequests( Principal principal,
                                                      @Parameter(description = "검색 조건 ", example = "아이디 or 닉네임 or 제출일 or 자격증 or 상태 or 전체")
                                                      @RequestParam(value = "searchType") String searchType,
                                                      @Parameter(description = "검색 키워드", example = "test1@test.com/test1/2023-08-31/반려동물종합관리사/WAITING/''")
                                                      @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                      @Parameter(hidden = true)
                                                      @PageableDefault(page = 0,size = 5,sort = "id",direction = Sort.Direction.DESC) Pageable pageable) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        if (SearchType.CERTIFICATION.getSearchType().equals(searchType)) {
            try {
                Certification certification = Certification.valueOf(keyword);
            } catch (IllegalArgumentException ex) {
                return response.fail(HttpStatus.BAD_REQUEST, "'반려동물종합관리사' 또는 '반려동물행동교정'으로 검색해 주세요.");
            }
        } else if (SearchType.STATUS.getSearchType().equals(searchType)) {
            try {
                Status status = Status.valueOf(keyword);
            } catch (IllegalArgumentException ex) {
                return response.fail(HttpStatus.BAD_REQUEST, "'WAITING', 'APPROVAL', 'REFUSAL'로 검색해주세요.");
            }
        } else if (SearchType.SUBMITDATE.getSearchType().equals(searchType)) {
            return response.fail(HttpStatus.BAD_REQUEST, "yyyy-MM-dd의 형태로 검색해주세요.");
        }
        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        return verificationService.getVerificationRequests(searchType,keyword, pageable);
    }
}
