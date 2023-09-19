package com.example.petree.domain.dog.controller;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.dog.schema.DogsSchema;
import com.example.petree.domain.dog.schema.PossessionDogSchema;
import com.example.petree.domain.dog.schema.DogSchema;
import com.example.petree.domain.dog.service.DogService;
import com.example.petree.domain.main_breed.domain.Size;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.main_breed.schema.MainBreedSchema;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.dog.cotroller
 * fileName       : DogController
 * author         : jsc
 * date           : 2023/07/04
 * description    : 강아지 관련 컨트롤러
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/04        정세창            /dogs api 추가
 * 2023/07/06        정세창            /dog api 추가
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dogs")
@Tag(name = "강아지 분양하기", description = "강아지 분양 관련 API")
public class DogController {

    private final DogService dogService;
    private final Response response;
    private final BreederRepository breederRepository;
    private final MemberRepository memberRepository;

    /**
     *
     * @param principal
     * @param pageable Pageable 객체
     * @param dogTypeId 견종 식별자 id
     * @param verification 인증 브리더 필터링 조건. yes/no
     * @param isAvailable 입양가능 견종 필터링 조건
     * @param gender 성별 필터링 조건
     * @param size 크기 필터링 조건
     * @return
     */
    @GetMapping
    @Operation(        // swagger annotation
            summary = "강아지 리스트 조회",
            description = " - 필터링 조건에 따라 강아지 리스트를 반환한다. n번째 페이지 요청 시에는 query param으로 'page=n' 형식으로 요청하면 됨(0부터 시작)." +
                    " 필터링 조건인 query param을 추가하지 않으면 해당 조건은 무시된다." +
                    " 각 조건을 query string에 포함하지 않으면 해당 필터는 적용되지 않는다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강아지 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = DogsSchema.DogsSchema200.class))),
            @ApiResponse(responseCode = "400", description = "verification에 yes/no 이외의 값 설정됨",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400S.class)))
    })
    public ResponseEntity<?> getDogs(
            Principal principal,
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "견종 식별자 id", example = "1")
            @RequestParam(value = "dogTypeId", defaultValue = "") Long dogTypeId,
            @Parameter(description = "인증 브리더들의 보유 견종 필터링. 적용하려면 yes로 보내주면 됨", example = "yes")
            @RequestParam(name = "verification", defaultValue = "") String verification,
            @Parameter(description = "입양 가능한 강아지 필터링 여부. 적용하려면 true로 보내주면 됨", example = "true")
            @RequestParam(name = "isAvailable", defaultValue = "") Boolean isAvailable,
            @Parameter(description = "성별 필터링 조건. 수컷이면 MALE, 암컷이면 FEMALE로 설정하면 됨", example = "MALE")
            @RequestParam(name = "gender", defaultValue = "") Gender gender,
            @Parameter(description = "크기 필터링 조건. 크기에 따라 <EXTRA_SMALL, SMALL, MEDIUM, LARGE, UNKNOWN>", example = "SMALL")
            @RequestParam(name = "size", defaultValue = "") Size size) {

        if(!verification.equals("") && !verification.equals("yes") && !verification.equals("no")) {
            response.fail(HttpStatus.BAD_REQUEST, Map.of("verification", "yes/no 이외에 다른 값이 설정되었습니다."));
        }

        return dogService.getDogs(pageable, dogTypeId, verification, isAvailable, gender, size);
    }

    /**
     *
     * @param id 강아지 id
     * @return DetailDogDto가 담긴 Response 객체
     */
    @Operation(        // swagger annotation
            summary = "강아지 상세 정보 조회",
            description = "특정 강아지의 상세 정보를 반환한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강아지 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = DogSchema.DogSchema200.class))),
            @ApiResponse(responseCode = "500", description = "강아지 조회 실패(null)",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDog(
            @Parameter(description = "강아지 id", example = "1")
            @PathVariable("id") Long id) {

        return dogService.getDog(id);
    }

}
