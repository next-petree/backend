package com.example.petree.domain.dog.controller;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.dog.dto.SimpleDogDto;
import com.example.petree.domain.dog.repository.DogRepository;
import com.example.petree.domain.dog.schema.PossessionDogSchema;
import com.example.petree.domain.dog.service.DogService;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.service.MainBreedService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.dog.controller
 * fileName       : PossessionController
 * author         : 박수현
 * date           : 2023-08-09
 * description    : 컨트롤러 추가
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-08        이지수          보유 견종 CRUD 작성
 * 2023-07-11        이지수          보유 견종 이미지 업로드 추가
 * 2023-07-16        이지수          생성, 삭제, 수정 모두 가능
 *                                  (s3 버킷에는 삭제 반영 안됨)
 *                                  삭제, 수정은 추후에 보강
 * 2023-07-30        이지수          Swagger작성, 전반적인 수정
 * 2023-08-02        이지수          삭제, 수정 보강
 *                                  (출생일 받을 때 null값으로 반환됨)
 * 2023-08-03        이지수          출생일 받을 때 null값 반환되는 것 해결
 * 2023-08-09        박수현              최초 생성
 * 2023-08-15        이지수          수정 시 이미지 업로드가 반드시
 *                                  이루어질 필요가 없는 것으로 수정해야함
 * 2023-08-18        이지수          스키마 추가
 * 2023-08-29        이지수          create, update 분리
 * 2023-08-29        이지수          getDogs(브리더 본인의 보유견종 조회 시 검색 타입 추가 ,견종 검색 수정
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/breeder/dogs")
@Tag(name = "브리더 회원, 마이페이지 보유견종관리", description = "브리더 회원, 마이페이지 보유견종관리 API")
public class PossessionController {

    private final DogService dogService;
    private final DogRepository dogRepository;
    private final Response response;
    private final BreederRepository breederRepository;

    @GetMapping
    @Operation(
            summary = "보유 견종 조회",
            description = "브리더 자신의 보유 견종을 리스트를 반환한다. n번째 페이지 요청 시에는 query param으로 'page=n'같은 형식으로 요청하면 됨(0부터 시작)"+
                    "검색 종류가 전체일 경우엔 검색 키워드는 빈칸으로 둬야함. -> 나중에 의견 반영할 예정임"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브리더의 보유 견종 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = PossessionDogSchema.PosessionDogSchema200.class)))
    })
    public ResponseEntity<?> getDogs(
            Principal principal,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "검색 종류",example = "견종 or 이름 or 전체 ")
            @RequestParam(value = "searchType") String seachType,
            @Parameter(description = "검색 키워드", example = "푸들 / 쫑이/")
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Breeder breeder= breederRepository.findByEmail(principal.getName()).orElse(null);
        Page<SimpleDogDto> dtos = dogService.getDogsOfBreeder(pageable, breeder, seachType,keyword);
        return response.success(HttpStatus.OK, dtos);
    }
    @GetMapping(value = "/{id}")
    @Operation(
            summary = "수정에 필요한 보유 견종 상세 조회 ",
            description = "브리더 본인의 보유견종에 대한 단건 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보유 견종 상세 조회 성공(보유 견종 수정 시 사용)",
                    content = @Content(schema = @Schema(implementation = PossessionDogSchema.PosessionDogSchema200.class))),
            @ApiResponse(responseCode = "400",description = "조회는 해당 보유 견종의 브리더만 가능합니다..",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))),
    })
    public ResponseEntity<?> getDogToUpdate(Principal principal,
                                    @Parameter(description = "보유 견종의 id 값",example = "1", required = true, in = ParameterIn.PATH )
                                    @PathVariable Long id){
        if (principal == null) {
            throw new MissingPrincipalException();
        }
        Breeder breeder = breederRepository.findByEmail(principal.getName()).orElse(null);
        if (breeder != null) {
            return response.success(HttpStatus.OK,dogService.getDogToUpdate(id));

        }else {
            return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "브리더 회원이 아닙니다."));}
    }


    @PostMapping(consumes="multipart/form-data")
    @Operation(
            summary = "보유 견종 등록",
            description = "보유 견종의 정보와 이미지를 등록할 수 있다.(이미지 등록 필수)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보유 견종 등록 성공.OK",
                    content = @Content(schema = @Schema(implementation = PossessionDogSchema.PosessionDogSchema200.class))),
            @ApiResponse(responseCode = "400",description = "보유 견종 등록은 브리더만 가능합니다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))),

    })
    public ResponseEntity<?> create(Principal principal,
                                    @Parameter(description = "선택한 주력 견종의 DogTypeId")
                                    @RequestParam Long dogTypeId,
                                    @Parameter(description = "보유 견종 정보 요청")
                                    @ModelAttribute @Valid PossessionDogDto.CreateDogDto possessionDogDto,
                                    BindingResult bindingResult
                                    ) {
        if (principal == null) {
            throw new MissingPrincipalException();
        }
        Breeder breeder = breederRepository.findByEmail(principal.getName()).orElse(null);
        if (breeder != null) {
            // 유효성 검사 에러 확인
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError fieldError : bindingResult.getFieldErrors()) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return response.fail(HttpStatus.BAD_REQUEST, errors);
            }

            dogService.create(breeder, dogTypeId, possessionDogDto);
            return response.success(HttpStatus.OK, possessionDogDto);
        }else {
            return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "브리더 회원이 아닙니다."));}
    }

    @PatchMapping(value = "/{id}",consumes="multipart/form-data")
    @Operation(
            summary = "보유 견종 정보 수정",
            description = "정보를 수정한다. 수정 시, 이미지 파일의 파일명을 받아서 선택적으로 삭제할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보유 견종 수정 성공",
                    content = @Content(schema = @Schema(implementation = PossessionDogSchema.PosessionDogSchema200.class))),
            @ApiResponse(responseCode = "400",description = "보유 견종에 대한 수정은 해당 보유견종의 브리더 회원만 가능합니다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class)))
    })
    public ResponseEntity<?> update(Principal principal,
                                    @Parameter(description = "보유 견종 id", example = "1", required = true, in = ParameterIn.PATH)
                                    @PathVariable Long id,
                                    @Parameter(description = "보유 견종 수정 정보 요청")
                                    @ModelAttribute PossessionDogDto.UpdateDogDto possessionDogDto) {
        if (principal == null) {
            throw new MissingPrincipalException();
        }
        Breeder breeder= breederRepository.findByEmail(principal.getName()).orElse(null);
        if(breeder != null) {

            dogService.deleteAndAddImages(breeder,id,possessionDogDto);

            dogService.update(breeder ,id, possessionDogDto);

            return response.success(HttpStatus.OK, possessionDogDto);

        }else {
            return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "브리더 회원이 아닙니다."));}
    }


    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "보유 견종 정보 삭제",
            description = "보유 견종에 대한 글을 삭제한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보유 견종 삭제 성공",
                    content = @Content(schema = @Schema(implementation = PossessionDogSchema.PosessionDogSchema200.class))),
            @ApiResponse(responseCode = "400",description = "보유 견종에 대한 삭제는 해당 보유견종의 브리더만 가능합니다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class)))

    })
    public ResponseEntity<?> delete(Principal principal,
                                    @Parameter(description = "보유 견종 id", example = "1", required = true, in = ParameterIn.PATH)
                                    @PathVariable Long id){
        if (principal == null) {
            throw new MissingPrincipalException();
        }
        Breeder breeder= breederRepository.findByEmail(principal.getName()).orElse(null);
        if(breeder != null) {

            dogService.delete(breeder,id);

            return response.success(HttpStatus.OK);

        }else {
            return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "브리더 회원이 아닙니다."));}
    }
}
