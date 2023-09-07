package com.example.petree.domain.breeder.controller;

import com.example.petree.domain.breeder.dto.BreederDetailDto;
import com.example.petree.domain.breeder.dto.BreederDto;
import com.example.petree.domain.breeder.schema.BreederSchema;
import com.example.petree.domain.breeder.service.BreederService;
import com.example.petree.domain.dog.schema.DogsSchema;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.dto.ProfileDto;
import com.example.petree.domain.member.schema.ProfileSchema;
import com.example.petree.domain.member.service.ProfileService;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.jwt.service.JwtTokenProvider;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/breeders")
@Tag(name = "브리더 둘러보기", description = "브리더 조회 API")
public class BreederController {

    private final BreederService breederService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Response response;

    /**
     * @author 박수현
     * @date 2023-07-13
     * @description : 사용자가 로그인하였다면, 해당 사용자를 기준으로 가까운 순으로 브리더 목록 조회,
     *                사용자가 로그인하지 않았다면 브리더를 id순으로 조회
     *                swagger에 로그인한 사용자는 헤더에 Authorization : Bearer AccessToken라는 형식으로 보내야함을 명시하기 위해
     *                HttpServletRequest방식을 취하지 않음.
     * @return
     */

    @GetMapping
    @Operation(
            summary = "브리더 조회",
            description = "로그인한 사용자는 거리순으로 브리더를 조회 <br>" +
                    "로그인하지 않은 사용자는 id순으로 브리더를 조회하며 거리정보는 null로 반환 <br>" +
                    "필터링/검색 조건에 따라 브리더 리스트를 반환 <br>" +
                    "n번째 페이지 요청 시에는 query param으로 'page=n'같은 형식으로 요청하면 됨(0부터 시작) <br> " +
                    "검색키워드도 query parm으로 요청하면 됨. ex) ?page=0 / ?verification에=yes&keyword=푸 <br>" +
                    "로그인한 사용자는 헤더에 Authorization : Bearer AccessToken을 요청해야하고, <br>" +
                    "로그인하지 않은 사용자는 헤더정보를 요청할 필요 없음."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "브리더둘러보기 조회 성공",
                    content = @Content(schema = @Schema(implementation = BreederSchema.BreedersSchema.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "verification에 yes 이외의 값 설정됨",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400S.class)))
    })
    public ResponseEntity<?> getBreeders(
            @Parameter(description = "Access Token", example = "Bearer AccessToken")
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PageableDefault(page = 0, size = 8) Pageable pageable,
            @Parameter(description = "인증 브리더 필터링 여부", example = "yes형식으로 보내야함.")
            @RequestParam(name = "verification", required = false, defaultValue = "") String verification,
            @Parameter(description = "검색 키워드", example = "푸들")
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) throws IOException {


        if (!verification.equals("") && !verification.equals("yes")) {
            return response.fail(HttpStatus.BAD_REQUEST, "yes 이외에 다른 값이 설정되었습니다.");
        }

        Page<BreederDto> breedersPage;

        log.info("verification 정보 : " + verification);
        log.info("keyword 정보 : " + keyword);

        if(authorizationHeader != null){
            String accessToken = authorizationHeader.split(" ")[1];
            Member member = jwtTokenProvider.getMember(accessToken);
            log.info("회원정보 : " + member);
            //Member member = memberService.getMember(httpServletRequest);
            breedersPage = breederService.listByDistance(member, pageable, verification, keyword);
        } else {
            breedersPage = breederService.list(pageable, verification, keyword);
        }
        return response.success(HttpStatus.OK, breedersPage);
    }

    /**
     * @author 박수현
     * @date 2023-08-22
     * @description : 제3자가 보는 브리더 프로필 상세 조회
     * @return
     */


    @GetMapping("/{id}")
    @Operation(
            summary = "브리더 상세 조회",
            description = "로그인 여부와 상관없이 브리더 상세 조회 가능"
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BreederSchema.BreederDetailSchema.class)))
    public ResponseEntity<?> getBreederById(@PathVariable Long id) {
        BreederDetailDto breederDetailDto = breederService.getProfileById(id);
        return response.success(HttpStatus.OK, breederDetailDto);
    }

}
