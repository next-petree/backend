package com.example.petree.domain.member.controller;

import com.example.petree.domain.breeder.schema.BreederSchema;
import com.example.petree.domain.breeder.service.BreederService;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.dto.MemberDto;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.dto.ProfileDto;
import com.example.petree.domain.member.schema.ProfileSchema;
import com.example.petree.domain.member.service.ProfileService;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.ErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.profile
 * fileName       : ProfileController
 * author         : 박수현
 * date           : 2023-06-30
 * description    : 프로필
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-30        박수현              최초 생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/me/profile")
@Tag(name = "마이페이지 내 프로필관리", description = "마이페이지 내 프로필관리 관련 API")
public class ProfileController {

    private final Response response;
    private final ProfileService profileService;


    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 자신의 프로필 이미지 조회
     * @return
     */

    @Operation(
            summary = "프로필이미지 조회 API",
            description = "로그인한 사용자는 자신의 프로필 이미지를 조회할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 이미지 조회 성공",
                    content = @Content(schema = @Schema(implementation = ProfileSchema.ProfileImgSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면 프로필 이미지 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    @GetMapping("/image")
    public ResponseEntity<?> getProfileImage(Principal principal) {
        if (principal == null) {
            throw new MissingPrincipalException();
        }

        ProfileDto.ProfileImgResponseDto profileImgResponseDto = profileService.getProfileImgDto(principal);
        return response.success(HttpStatus.OK, profileImgResponseDto);
    }



    /**
     * @author 박수현
     * @date 2023-08-03
     * @description : 프로필 이미지 업로드
     * @return Response
     */

    @Operation(
            summary = "프로필이미지 업로드 API",
            description = "로그인한 사용자는 프로필 이미지를 업로드 할 수 있다. <br>" +
                    "이미 프로필이 있는 사용자가 다른 프로필 이미지를 업로드하고자 한다면, 요청 프로필이미지가 업로드된다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 이미지 업로드 성공",
                    content = @Content(schema = @Schema(implementation = ProfileSchema.ProfileImgSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면 프로필 이미지 업로드 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    @PostMapping("/image/upload")
    public ResponseEntity<?> uploadProfileImage(
            @Parameter(description = "업로드할 이미지 파일", example = "example.jpg",required = true)
            @RequestParam("image") MultipartFile multipartFile, Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        ProfileDto.ProfileImgResponseDto uploadedImage = profileService.uploadProfileImage(multipartFile, principal);
        return response.success(HttpStatus.OK,uploadedImage);
    }

    /**
     * @author 박수현
     * @date 2023-08-03
     * @description : 프로필 이미지 삭제
     * @return Response
     */

    @Operation(
            summary = "프로필이미지 삭제 API",
            description = "로그인한 사용자는 프로필 이미지 삭제 가능하다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 이미지 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면 프로필 이미지 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    @DeleteMapping("/image/{profileImgId}")
    public ResponseEntity<?> deleteProfileImage(
            Principal principal,
            @Parameter(description = "프로필이미지 ID", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable Long profileImgId) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        profileService.deleteProfileImage(profileImgId);
        return response.success(HttpStatus.OK, "프로필 이미지가 삭제되었습니다.");
    }

    /**
     * @author 정세창
     * @date 2023-08-24
     * @description : 자기소개 조회
     * @return
     */


    @GetMapping("/self-introduction")
    @Operation(
            summary = "자기소개 조회",
            description = "본인의 자기소개 내용을 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "자기소개 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면 본인의 자기소개 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> getMyIntroduction(Principal principal) {

        String myIntroduction = profileService.getMyIntroduction(principal);

        return response.success(HttpStatus.OK, myIntroduction);
    }


    /**
     * @author 정세창
     * @date 2023-08-24
     * @description : 자기소개 수정
     * @return
     */

    @PutMapping("/self-introduction")
    @Operation(
            summary = "자기소개 수정",
            description = "본인의 자기소개 내용을 수정한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "자기소개 등록 혹은 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면 본인의 자기소개 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> getMyIntroduction(Principal principal, @RequestBody Map<String, String> body) {

        profileService.updateMyIntroduction(principal, body.get("content"));

        return response.success(HttpStatus.OK);
    }


}
