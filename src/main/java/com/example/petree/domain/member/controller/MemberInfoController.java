package com.example.petree.domain.member.controller;

import com.example.petree.domain.member.dto.ProfileDto;
import com.example.petree.domain.member.schema.ProfileSchema;
import com.example.petree.domain.member.service.ProfileService;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.ErrorResponse;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

/**
 * packageName    : com.example.petree.domain.member.controller
 * fileName       : MemberInfoController
 * author         : 박수현
 * date           : 2023-08-28
 * description    : 마이페이지 내 회원정보 수정
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/me/member-info")
@Tag(name = "마이페이지 내 회원정보수정", description = "마이페이지 내 회원정보수정관련 API")
public class MemberInfoController {

    private final Response response;
    private final ProfileService profileService;

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 회원정보 조회
     * @return
     */
    @GetMapping
    @Operation(
            summary = "자신의 회원정보 조회",
            description = "로그인한 사용자는 자신의 회원정보를 조회할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ProfileSchema.PersonalInfoRequestDto200.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면, 프로필 정보 반환 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> getMyProfile(Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        ProfileDto.PersonalInfoResponseDto personalInfoResponseDto = profileService.getMemberInfo(principal);

        return response.success(HttpStatus.OK, personalInfoResponseDto);
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 회원정보 변경
     * @return
     */

    @PatchMapping
    @Operation(
            summary = "자신의 회원정보 수정",
            description = "로그인한 사용자는 자신의 회원정보를 수정할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 하지 않았다면, 프로필 정보 반환 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> updateMemberInfo(
            Principal principal,
            @RequestBody @Valid ProfileDto.PersonalInfoRequestDto updateDto,
            BindingResult bindingResult) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        try {
            if (updateDto.getNickname() != null && !updateDto.isNicknameChecked()) {
                return response.fail(HttpStatus.BAD_REQUEST, "닉네임 중복확인이 필요합니다.");
            }
            // 유효성 검사를 통과하지 못한 경우 바로 에러 메시지 반환
            if (bindingResult.hasErrors()) {
                Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
                return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
            }
            profileService.updateMemberInfo(principal, updateDto);
            return response.success(HttpStatus.OK, "회원정보가 성공적으로 수정되었습니다.");
        } catch (Exception ex) {
            return response.error(ex.getMessage());
        }
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 비밀번호 변경
     * @return
     */


    @PutMapping("/pwd/change")
    @Operation(
            summary = "비밀번호 변경",
            description = "현재 비밀번호를 입력하고 새로운 비밀번호를 설정한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 변경 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ProfileDto.PasswordChangeRequestDto request,
            BindingResult bindingResult,
            Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
            return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
            return response.fail(HttpStatus.BAD_REQUEST, "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        profileService.changePassword(principal, request.getCurrentPassword(), request.getNewPassword());

        return response.success(HttpStatus.OK, "비밀번호가 성공적으로 수정되었습니다.");
    }
}
