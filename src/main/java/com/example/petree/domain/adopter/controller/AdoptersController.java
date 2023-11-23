package com.example.petree.domain.adopter.controller;

import com.example.petree.domain.adopter.dto.AdopterDetailDto;
import com.example.petree.domain.adopter.repository.AdopterRepository;
import com.example.petree.domain.adopter.schema.AdopterSchema;
import com.example.petree.domain.adopter.service.AdopterService;
import com.example.petree.global.Response;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.example.petree.domain.adopter.controller
 * fileName       : AdoptersController
 * author         : 박수현
 * date           : 2023-08-28
 * description    : 제 3자가 보는 분양희망자 프로필 조회
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/adopters")
@Tag(name = "분양희망자 조회", description = "제3자가 보는 분양희망자 조회 API")
public class AdoptersController {

    private final AdopterService adopterService;
    private final Response response;

    @GetMapping("/{id}")
    @Operation(
            summary = "분양희망자 상세 조회",
            description = "로그인 여부와 상관없이 브리더 상세 조회 가능"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양희망자 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = AdopterSchema.class))
            )
    })
    public ResponseEntity<?> getBreederById(@PathVariable Long id) {

        AdopterDetailDto adopterDetailDto = adopterService.getProfileById(id);
        return response.success(HttpStatus.OK, adopterDetailDto);
    }
}
