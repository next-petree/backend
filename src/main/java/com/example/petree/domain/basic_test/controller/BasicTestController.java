package com.example.petree.domain.basic_test.controller;

import com.example.petree.domain.adopter.repository.AdopterRepository;
import com.example.petree.domain.basic_test.dto.BasicTestDto;
import com.example.petree.domain.basic_test.dto.TestResultDto;
import com.example.petree.domain.basic_test.schema.BasicTestResultSchema;
import com.example.petree.domain.basic_test.schema.BasicTestSchema;
import com.example.petree.domain.basic_test.service.BasicTestService;
import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.exception.MissingPrincipalException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * packageName    : com.example.petree.domain.basic_test.controller
 * fileName       : BasicTestController
 * author         : 박수현
 * date           : 2023-07-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-30        박수현              최초 생성
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "기초지식테스트", description = "기초지식테스트 조회/채점 및 해설제공 API")
@RequestMapping("/basic-test")
public class BasicTestController {

    private final BasicTestService basicTestService;
    private final MemberRepository memberRepository;
    private final Response response;
    private final AdopterRepository adopterRepository;

    /**
     * @author 박수현
     * @date 2023-08-05
     * @description : 기초지식테스트 조회
     * @return
     */

    @GetMapping("/start")
    @Operation(summary = "기초 지식 테스트 조회", description = "분양희망자 회원은 기초지식테스트를 실시할 수 있다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "기초지식테스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = BasicTestSchema.BasicTestSchema200.class)))
            ,
            @ApiResponse(
                    responseCode = "401",
                    description = "분양희망자 회원이 아니거나 로그인이 필요한 경우 기초지식테스트 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> startBasicTest(Principal principal) {
        if (principal == null) {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        if(member != null){
            if(member.getRole().getTitle().equals("ADOPTER")){
                BasicTestDto basicTestDto = basicTestService.startBasicTest();
                return response.success(HttpStatus.OK, basicTestDto);
            } else {
                return response.fail(HttpStatus.UNAUTHORIZED, "분양희망자 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    /**
     * @author 박수현
     * @date 2023-08-06
     * @description : 기초지식테스트 결과 반환
     * @return
     */

    @PostMapping("/submit")
    @Operation(
            summary = "기초 지식 테스트 결과 반환",
            description = "분양희망자 회원은 기초지식테스트 결과를 확인할 수 있다. <br>" +
                          "결과는 점수, PASS/FAIL여부, 틀린 문항이 있다면, 틀린문항에 대한 해설을 확인할 수 있다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "기초지식테스트 결과 반환",
                    content = @Content(schema = @Schema(implementation = BasicTestResultSchema.BasicTestResultSchema200.class)))
            ,
            @ApiResponse(
                    responseCode = "401",
                    description = "분양희망자 회원이 아니거나 로그인이 필요한 경우 기초지식테스트 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> submitTestResult(@RequestBody TestResultDto.TestResultRequestDto requestDto, Principal principal) {
        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        if(member != null){
            if(member.getRole().getTitle().equals("ADOPTER")){
                Adopter adopter = (Adopter) member;
                TestResultDto.TestResultResponseDto resultDto = basicTestService.submitTestResult(requestDto);
                if(resultDto.isPassed()){
                    adopter.setIsVerified(true);
                    adopterRepository.save(adopter);
                    log.info("인증됨");
                }
                return ResponseEntity.ok(resultDto);
            } else {
                return response.fail(HttpStatus.UNAUTHORIZED, "분양희망자 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }
}
