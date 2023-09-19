package com.example.petree.domain.main_breed.controller;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.main_breed.schema.MainBreedSchema;
import com.example.petree.domain.main_breed.service.MainBreedService;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.ErrorResponse;
import com.example.petree.global.error.exception.MissingPrincipalException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/breeder/main-breeds")
@Tag(name = "브리더 회원, 마이페이지 프로필관리의 주력견종", description = "브리더 회원, 마이페이지 프로필관리의 주력견종 관련 API")
public class MainBreedController {

    private final MainBreedService mainBreedService;
    private final Response response;
    private final MemberRepository memberRepository;
    private final BreederRepository breederRepository;


    @GetMapping
    @Operation(
            summary = "브리더회원의 주력견종 목록 반환",
            description = "주력 견종 목록 반환"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주력견종 목록 반환 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MainBreedSchema.MainBreedListSchema200.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면, 주력견종 목록 반환 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> index(Principal principal) throws IOException {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Breeder breeder = breederRepository.findByEmail(principal.getName()).orElse(null);

        if (breeder != null) {
            if (breeder.getRole().getTitle().equals("BREEDER")) {
                List<MainBreedDto.MainBreedDtoResponse> mainBreedDtos = mainBreedService.index(breeder);
                return response.success(HttpStatus.OK, mainBreedDtos);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, "브리더 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }


    /**
     * @author 박수현
     * @date 2023-08-02
     * @description : 작성자에 따른 주력견종 단일 정보 반환
     * @return
     */
    /*@GetMapping("/mainBreeds/{mainBreedId}")
    @Operation(
            summary = "로그인한 브리더회원의 주력견종 단일 정보 반환",
            description = "로그인한 브리더회원의 주력견종 단일 정보 반환"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주력견종 단일 정보 반환 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MainBreedSchema.MainBreedSchema200.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "브리더 회원이 아니거나 로그인이 필요한 경우 주력견종 단일 정보 반환 실패",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class)))
            )
    })
    public ResponseEntity<?> show(
            @Parameter(description = "주력견종 ID", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable Long mainBreedId, Principal principal) throws IOException {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        if(member != null){
            if(member.getRole().getTitle().equals("BREEDER")){
                MainBreedDto.MainBreedDtoResponse mainBreedDto = mainBreedService.show(member, mainBreedId);
                return response.success(HttpStatus.OK, mainBreedDto);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, Map.of("message", "브리더 회원이 아닙니다."));
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, Map.of("message", "로그인이 필요합니다."));
        }
    }*/


    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 작성자에 따른 주력견종 데이터 등록
     * @return ResponseEntity<MainBreedDto>
     */

    @PostMapping
    @Operation(
            summary = "주력견종 추가",
            description = "주력견종 추가"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주력견종 추가 성공",
                    content = @Content(schema = @Schema(implementation = MainBreedSchema.MainBreedListSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "브리더 회원이 아니거나 로그인이 필요한 경우 주력견종 정보 등록 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> create(
            Principal principal,
            @RequestBody MainBreedDto.MainBreedDtoRequest mainBreedDtoRequest) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Breeder breeder = breederRepository.findByEmail(principal.getName()).orElse(null);
        if(breeder != null){
            int maxMainBreeds = 3;
            int existingMainBreeds = breeder.getMainBreeds().size();

            List<Long> newDogTypeIds = mainBreedDtoRequest.getDogTypeId().stream()
                    .filter(dogTypeId -> !mainBreedService.exists(breeder.getId(), dogTypeId))
                    .collect(Collectors.toList());

            int newMainBreedsCount = newDogTypeIds.size();

            if (existingMainBreeds + newMainBreedsCount > maxMainBreeds) {
                return response.fail(HttpStatus.BAD_REQUEST, "주력견종은 최대 3개까지 등록할 수 있습니다.");
            }

            List<MainBreedDto.MainBreedDtoResponse> createdMainBreeds =
                    mainBreedService.create(breeder, newDogTypeIds);

            return response.success(HttpStatus.CREATED, createdMainBreeds);
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 작성자에 따른 주력견종 데이터 삭제
     * @return ResponseEntity<MainBreedDto>
     */
    /*@DeleteMapping("/mainBreeds/{dogTypeId}")
    @Operation(
            summary = "등록한 주력 견종 삭제",
            description = "등록한 주력 견종 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주력견종 정보 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "브리더 회원이 아니거나 로그인이 필요한 경우 주력견종 정보 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "주력견종 ID", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable Long dogTypeId, Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Breeder breeder = breederRepository.findByEmail(principal.getName()).orElse(null);
        if(breeder != null){
            mainBreedService.delete(breeder, dogTypeId);
            return response.success(HttpStatus.OK);
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, Map.of("message", "로그인이 필요합니다."));
        }
    }*/

    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 작성자에 따른 주력견종 데이터 수정
     * @return ResponseEntity<MainBreedDto>
     */

    @PatchMapping
    @Operation(
            summary = "주력견종 정보 수정",
            description = "주력견종 정보 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주력견종 정보 수정 성공",
                    content = @Content(schema = @Schema(implementation = MainBreedSchema.MainBreedListSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "브리더 회원이 아니거나 로그인이 필요한 경우 주력견종 정보 수정 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> update(
            Principal principal,
            @RequestBody MainBreedDto.MainBreedDtoRequest mainBreedDtoRequest) throws IOException {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        if(member != null){
            if(member.getRole().getTitle().equals("BREEDER")){
                List<MainBreedDto.MainBreedDtoResponse> mainBreedDtoResponseList = mainBreedService.update(member, mainBreedDtoRequest);
                return response.success(HttpStatus.OK, mainBreedDtoResponseList);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, "브리더 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }
}
