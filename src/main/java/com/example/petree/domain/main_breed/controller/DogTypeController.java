package com.example.petree.domain.main_breed.controller;

import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.dto.DogTypeDto;
import com.example.petree.domain.main_breed.repository.DogTypeRepository;
import com.example.petree.domain.main_breed.schema.AllDogTypesSchema;
import com.example.petree.domain.main_breed.schema.DogTypeSchema;
import com.example.petree.domain.main_breed.schema.MainBreedSchema;
import com.example.petree.domain.main_breed.service.DogTypeService;
import com.example.petree.domain.matching.schema.PostResultSchema;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.main_breed.controller
 * fileName       : DogTypeController
 * author         : 박수현
 * date           : 2023/08/14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/14         박수현             최초생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/dog-type")
@Tag(name = "견종 검색", description = "견종 검색")
public class DogTypeController {

    private final DogTypeService dogTypeService;
    private final Response response;

    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 견종 정보 리스트 조회
     * @return
     */

    @GetMapping
    @Operation(
            summary = "견종 목록 반환",
            description = "모든 견종의 목록을 반환."
    )
    @ApiResponse(
            responseCode = "200",
            description = "견종 목록 반환 성공",
            content = @Content(schema = @Schema(implementation = DogTypeSchema.DogTypeListSchema200.class))
    )
    public ResponseEntity<?> getAllDogTypes() {
        List<DogTypeDto> dogTypes = dogTypeService.getAllDogTypes();
        return response.success(HttpStatus.OK, dogTypes);
    }

    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 견종 정보 검색어 이용
     * @return
     */

    @GetMapping("/search")
    @Operation(
            summary = "견종 검색",
            description = "견종 이름을 키워드로 검색하여 검색 결과를 반환."
    )
    @ApiResponse(
            responseCode = "200",
            description = "견종 검색 결과 반환 성공",
            content = @Content(schema = @Schema(implementation = DogTypeSchema.DogTypeListSchema200.class))
    )
    public ResponseEntity<?> searchDogTypes(@RequestParam String keyword) {
        log.info("키워드 : " + keyword);
        if (keyword == null || keyword.isEmpty()) {
            return response.success(HttpStatus.OK, null);
        }
        List<DogTypeDto> dogTypes = dogTypeService.searchDogTypesByKeyword(keyword);
        return response.success(HttpStatus.OK, dogTypes);
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 견종 추가
     * @return
     */

    @GetMapping("/add")
    @Operation(
            summary = "희귀 견종 등록",
            description = "서버에 존재하지 않는 새로운 견종을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "희귀 견종 등록 성공",
                    content = @Content(schema = @Schema(implementation = DogTypeSchema.DogTypeSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "견종 이름이 비어있거나 이미 견종이 존재하는 경우",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400S.class))
            )
    })
    public ResponseEntity<?> addDogType(
            @Parameter(description = "등록할 견종 이름", required = true) @RequestParam String dogType){
        if(dogType == null || dogType.isEmpty()){
            return response.fail(HttpStatus.BAD_REQUEST, "빈 문자열입니다.");
        } else {
            DogTypeDto addDogType = dogTypeService.addDogType(dogType);
            return response.success(HttpStatus.OK, addDogType);
        }
    }
}
