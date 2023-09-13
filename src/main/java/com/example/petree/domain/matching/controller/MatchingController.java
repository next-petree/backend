package com.example.petree.domain.matching.controller;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.adopter.dto.ReviewDto;
import com.example.petree.domain.matching.domain.Search;
import com.example.petree.domain.matching.domain.SearchType;
import com.example.petree.domain.matching.dto.MatchingFormDto;
import com.example.petree.domain.matching.schema.*;
import com.example.petree.domain.matching.service.MatchingService;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import com.example.petree.domain.adopter.repository.AdopterRepository;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.ErrorResponse;
import com.example.petree.global.error.exception.MissingPrincipalException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.matching.controller
 * fileName       : MatchingController
 * author         : jsc
 * date           : 2023/07/09
 * description    : 실제 강아지 매칭 관련 컨트롤러
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/09        jsc                init
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "마이페이지 내 분양신청내역확인", description = "마이페이지 내 분양신청내역확인 관련 API")
@RequestMapping("/me/matchings")
public class MatchingController {

    private final MatchingService matchingService;
    private final MemberRepository memberRepository;
    private final AdopterRepository adopterRepository;
    private final Response response;

    /**
     *
     * @param principal Principal 객체
     * @param formDto 매칭 신청 내용 및 이미지가 포함된 신청 form
     * @return 성공 여부
     */
    @PostMapping
    @Operation(        // swagger annotation
            summary = "매칭 신청",
            description = "브리더에게 매칭을 신청한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 신청 성공",
                    content = @Content(schema = @Schema(implementation = PostResultSchema.PostResultSchema200.class))),
            @ApiResponse(responseCode = "500", description = "엔티티 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다.\" +\n" +
                    "                            \"요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class)))
    })
    public ResponseEntity<?> addMatching(
            Principal principal,
            @Parameter(description = "multipart form 요청")
            @RequestBody @Valid MatchingFormDto formDto,
            BindingResult bindingResult) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
            return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
        }

        Adopter adopter = adopterRepository.findByEmail(principal.getName()).orElse(null);
        return matchingService.addMatching(adopter, formDto);
    }

    /**
     *
     * @param principal Principal 객체
     * @param pageable @PageableDefault로 설정된 기본 Pageable 객체
     * @param
     * @return Page로 가공한 매칭 신청 리스트가 담긴 Response 객체
     */
    @GetMapping
    @Operation(        // swagger annotation
            summary = "매칭 리스트 조회",
            description = "자신의 매칭 리스트를 조회한다. <br> "
            + "전체일 경우는 query param없이 요청한다."
            + "검색타입을 이용한다면, query param으로 이용한다. <br> 견종을 이용하여 검색시, ?searchType=type&keyword=푸들 OR" +
            "<br> 강아지 이름을 이용하여 검색시, ?searchType=name&keyword=쫑이 <br>"
            + "n번째 페이지 요청 시에는 query param으로 'page=n'같은 형식으로 요청하면 됨(0부터 시작)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 조회 성공(현재 예시는 브리더가 조회한 상황, 입양희망자가 조회 시 'adopter' 부분을 'breeder'로 치환)",
                    content = @Content(schema = @Schema(implementation = MatchingsOfBreederSchema.MatchingsSchema200.class)))
    })
    public ResponseEntity<?> getMatchings(
            Principal principal,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 4, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute Search search){

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        if(member == null) return response.error("회원이 조회되지 않습니다.");

        if (member.getRole() == Role.BREEDER) {
            return matchingService.getMatchingsOfBreeder(pageable, member, search);
        } else{
            return matchingService.getMatchingsOfAdopter(pageable, member, search);
        }
    }

    @GetMapping("/{matchingId}")
    @Operation(        // swagger annotation
            summary = "매칭 상세 조회",
            description = "매칭 상세 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "- breeder회원일 경우, 매칭 상세 정보는 분양신청서의 문항까지 조회된다. <br>"
                    + "adopter회원일 경우, 승인일때만 상세조회가 가능하다. 거절일 경우는 모두 null로 반환한다.",
                    content = @Content(schema = @Schema(implementation = MatchingOfBreederSchema.MatchingSchema200.class))),
            @ApiResponse(responseCode = "500", description = "엔티티 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class)))
    })
    public ResponseEntity<?> getMatching(
            Principal principal,
            @Parameter(description = "매칭 id", example = "1")
            @PathVariable("matchingId") Long matchingId
    ) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (member.getRole() == Role.BREEDER) {
            return response.success(
                    HttpStatus.OK,
                    matchingService.getMatchingOfBreeder(matchingId)
            );
        } else{
            return matchingService.getMatchingOfAdopter(matchingId);
        }
    }

    @PostMapping("/{matchingId}/approval")
    @Operation(        // swagger annotation
            summary = "매칭 신청 처리",
            description = "특정 매칭 신청에 대한 처리를 진행한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 신청 처리 완료",
                    content = @Content(schema = @Schema(implementation = PostResultSchema.PostResultSchema200.class))),
            @ApiResponse(responseCode = "500", description = "엔티티 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class)))
    })
    public ResponseEntity<?> processMatching(
            @Parameter(description = "매칭 id", example = "1")
            @PathVariable(value = "matchingId") Long matchingId,
            @Parameter(description = "승인/반려 여부", example = "true or false")
            @RequestParam(value = "isApproved") Boolean isApproved, Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (member != null) {
            if (member.getRole().getTitle().equals("BREEDER")) {
                return matchingService.processMatching(matchingId, isApproved);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, "브리더 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

    }

}
