package com.example.petree.domain.adopter.controller;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.adopter.dto.ResidentialEnvDto;
import com.example.petree.domain.adopter.repository.AdopterRepository;
import com.example.petree.domain.adopter.schema.ResidentialEnvSchema;
import com.example.petree.domain.adopter.service.AdopterService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.member.controller
 * fileName       : AdopterController
 * author         : 박수현
 * date           : 2023-08-17
 * description    : 분양희망자의 프로필 조회
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-17        박수현              최초 생성
 */
 
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/adopter")
@Tag(name = "분양희망자 회원, 마이페이지 프로필관리의 주거환경", description = "분양희망자 회원, 마이페이지 프로필관리의 주거환경 관련 API")
public class AdopterController {

    private final AdopterService adopterService;
    private final Response response;
    private final AdopterRepository adopterRepository;


    @GetMapping("/residential-environments")
    @Operation(
            summary = "분양 희망자의 주거 환경 가져오기",
            description = "분양 희망자가 설정한 주거 환경 이미지 리스트를 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양희망자의 주거환경 이미지 리스트 조회 성공. 보이는 것과 달리 거실, 화장실, 마당 총 3개의 항목을 가진다.",
                    content = @Content(schema = @Schema(implementation = ResidentialEnvSchema.ResidentialEnvSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "분양 희망자 회원이 아니거나 로그인이 필요한 경우 분양희망자 정보 수정 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> getEnvironments(Principal principal) {

        Adopter adopter = adopterRepository.findByEmail(principal.getName()).orElse(null);
        if(adopter == null) {
            return response.error("로그인이 필요하거나, 분양 희망자 회원이 아닙니다.");
        }
        List<Object> environments = adopterService.getEnvironments(adopter);

        return response.success(HttpStatus.OK, environments);
    }


    @PutMapping(value = "/residential-environments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "분양 희망자의 주거 환경 이미지 업데이트(추가/수정/삭제)",
            description = "분양 희망자가 전송한 주거환경 이미지로 업데이트 한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양희망자의 주거환경 이미지 업데이트 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청값이 잘못되었을 때 수정 실패. 한 공간에 대해 중복 등록 시도, 없는 파일 삭제 시도 등",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400S.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "분양 희망자 회원이 아니거나 로그인이 필요한 경우 분양희망자 정보 수정 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "엔티티 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    public ResponseEntity<?> addOrUpdateEnvironments(Principal principal,
                                                     @ModelAttribute ResidentialEnvDto.EnvRequestDto form) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Adopter adopter = adopterRepository.findByEmail(principal.getName()).orElse(null);
        adopterService.updatePhotos(adopter, form);

        return response.success(HttpStatus.OK);
    }
}
