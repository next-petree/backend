package com.example.petree.domain.matching.controller;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.matching.dto.MatchingFormDto;
import com.example.petree.domain.matching.schema.*;
import com.example.petree.domain.matching.service.MatchingService;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import com.example.petree.domain.adopter.repository.AdopterRepository;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    @PostMapping("")
    @Operation(        // swagger annotation
            summary = "매칭 신청",
            description = "브리더에게 매칭을 신청한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 신청 성공",
                    content = @Content(schema = @Schema(implementation = PostResultSchema.PostResultSchema200.class))),
            @ApiResponse(responseCode = "500", description = "엔티티 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class)))
    })
    public ResponseEntity<?> addMatching(
            Principal principal,
            @Parameter(description = "multipart form 요청")
            @RequestBody MatchingFormDto formDto) {

        Adopter adopter = adopterRepository.findByEmail(principal.getName()).orElse(null);
        return matchingService.addMatching(adopter, formDto);
    }

    /**
     *
     * @param principal Principal 객체
     * @param pageable @PageableDefault로 설정된 기본 Pageable 객체
     * @param keyword 상대방의 닉네임이나 이메일 검색 키워드
     * @return Page로 가공한 매칭 신청 리스트가 담긴 Response 객체
     */
    @GetMapping("")
    @Operation(        // swagger annotation
            summary = "매칭 리스트 조회",
            description = "자신의 매칭 리스트를 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 조회 성공(현재 예시는 브리더가 조회한 상황, 입양희망자가 조회 시 'adopter' 부분을 'breeder'로 치환)",
                    content = @Content(schema = @Schema(implementation = MatchingsOfBreederSchema.MatchingsSchema200.class)))
    })
    public ResponseEntity<?> getMatchings(
            Principal principal,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "검색 키워드(상대방 이메일 or 닉네임)", example = "nickname or email")
            @RequestParam(value = "keyword", defaultValue = "") String keyword){

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (member.getRole() == Role.BREEDER) {
            return matchingService.getMatchingsOfBreeder(pageable, member, keyword);
        } else{
            return matchingService.getMatchingsOfAdopter(pageable, member, keyword);
        }
    }

    @GetMapping("/{matchingId}")
    @Operation(        // swagger annotation
            summary = "매칭 상세 조회",
            description = "특정 매칭 정보를 상세조회한다. matchingImgs는 매칭환경 이미지들의 url이다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "- 매칭 조회 성공(현재 예시는 브리더가 조회한 상황, 입양희망자가 조회 시 'adopter' 부분을 'breeder'로 치환)\n" +
                    "- '승인'상태가 아니라면 상대방의 nickname, phone number, address는 null로 반환된다.",
                    content = @Content(schema = @Schema(implementation = MatchingOfBreederSchema.MatchingSchema200.class))),
            @ApiResponse(responseCode = "500", description = "엔티티 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class)))
    })
    public ResponseEntity<?> getMatching(
            Principal principal,
            @Parameter(description = "매칭 id", example = "1")
            @PathVariable("matchingId") Long matchingId
    ) {

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
            @RequestParam(value = "isApproved") Boolean isApproved) {

        return matchingService.processMatching(matchingId, isApproved);
    }

}
