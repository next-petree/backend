package com.example.petree.domain.verification.controller;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;

import com.example.petree.domain.member.domain.Admin;
import com.example.petree.domain.member.domain.Member;

import com.example.petree.domain.member.domain.Role;
import com.example.petree.domain.member.repository.AdminRepository;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.verification.domain.Certification;
import com.example.petree.domain.verification.domain.Status;
import com.example.petree.domain.verification.dto.VerificationFormDto;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.verification.controller
 * fileName       : VerificationController
 * author         : 이지수
 * date           : 2023/08/07
 * description    : 브리더 인증에 관한 컨트롤러
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-07        이지수          최초생성
 * 브리더는 관리자에게 브리더 자격을 증명할 수 있는 파일을 첨부하여
 * 인증 요청을 보내고, 관리자는 승인,거절을 할 수 있음(관리자는 임시)
 * 2023-08-09       이지수          검색, 페이징 추가
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="브리더 인증하기")
public class VerificationController {
    private final VerificationService verificationService;
    private final BreederRepository breederRepository;
    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
    private final Response response;


    @PostMapping(value = "/verifications", consumes = "multipart/form-data")
    @Operation(
            summary = "브리더 인증 요청",
            description = "관리자에게 브리더 인증을 요청한다. 파일을 첨부해서 자격을 증명한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브리더 요청 성공",
                    content = @Content(schema = @Schema(implementation = VerificationSchema.Verification200.class))),
            @ApiResponse(responseCode = "400", description = "브리더 인증 요청은 브리더에게만 권한이 있습니다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))),
    })
    public ResponseEntity<?> addVerification(
            Principal principal,
            @Parameter(description = "브리더 자격 증명 파일 첨부 요청")
            @ModelAttribute VerificationFormDto verificationFormDto) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }
        Breeder breeder = breederRepository.findByEmail(principal.getName()).orElse(null);
        if(breeder != null) {
            Admin admin =adminRepository.findByRole(Role.ADMIN);
            if (admin != null) {
                verificationService.addVerification(breeder, verificationFormDto, admin);
                return response.success(HttpStatus.OK, verificationFormDto);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "관리자를 찾을 수 없습니다."));
            }
        } else {
            return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "브리더 회원이 아닙니다."));
        }
    }
}
