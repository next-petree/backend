package com.example.petree.domain.member.controller;

import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import com.example.petree.domain.member.dto.*;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.schema.LoginSchema;
import com.example.petree.domain.member.schema.MemberSchema;
import com.example.petree.domain.member.service.MemberService;
import com.example.petree.domain.member.service.SmsService;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.ErrorResponse;
import com.example.petree.global.error.exception.MissingPrincipalException;
import com.example.petree.global.jwt.service.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Map;

@Tag(name = "회원관리 API", description = "회원관리 관련 API")
@RequestMapping("/api")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final Response response;
    private final SmsService smsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    /**
     * @author 박수현
     * @date 2023-08-09
     * @description : 문자인증 SMS발송
     * @return
     */


    @PostMapping("/sms/send")
    @Operation(
            summary = "SMS 문자 발송 API",
            description = "문자 메시지 발송"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SMS 문자 발송 성공",
                    content = @Content(schema = @Schema(implementation = SmsDto.SmsResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            )
    })
    public ResponseEntity<?> sendSms(@Valid @RequestBody MessageDto messageDto, BindingResult bindingResult) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
            log.info("에러 탔음");
            return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            log.info("인증 탔음");
            SmsDto.SmsResponseDto code = smsService.sendSms(messageDto);
            return response.success(HttpStatus.OK, code);
        }
    }

    /**
     * @author 박수현
     * @date 2023-08-10
     * @description : 발송받은 인증코드 검증
     * @return
     */

    @PostMapping("/sms/verify")
    @Operation(
            summary = "SMS로 발송된 인증코드 검증 API",
            description = "SMS로 발송된 인증코드를 검증할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "검증 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "전화번호를 잘못 요청할 경우 400에러 반환",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    public ResponseEntity<?> verifySmsCode(@Valid @RequestBody VerifyDto verifyDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
            log.info("에러 탔음");
            return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
        }

        try {
            boolean isVerified = smsService.verifySmsCode(verifyDto.getPhoneNumber(), verifyDto.getCode());
            if (isVerified) {
                return response.success(HttpStatus.OK, "인증코드가 일치합니다.");
            } else {
                return response.fail(HttpStatus.OK, Map.of("code", "인증코드가 일치하지 않습니다."));
            }
        } catch (Exception e) {
            return response.fail(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류");
        }
    }

    /**
     * @author 박수현
     * @date 2023-08-17
     * @description : 닉네임 중복 여부 판단
     * @return
     */

    @PostMapping("/nickname/check")
    @Operation(
            summary = "닉네임 중복 확인",
            description = "회원가입시 닉네임 중복 여부를 판단할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입시 닉네임 중복 여부를 판단할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            )
    })
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        memberService.checkNickname(nickname);
        return response.success(HttpStatus.OK, "사용 가능한 닉네임입니다.");
    }


    /**
     * @author 박수현
     * @date 2023-08-17
     * @description : 아이디(이메일) 중복 여부 판단
     * @return
     */

    @PostMapping("/email/check")
    @Operation(
            summary = "아이디(이메일) 중복 확인",
            description = "회원가입시 아이디(이메일) 중복 여부를 판단할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입시 아이디(이메일) 중복 여부를 판단할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            )
    })
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        memberService.checkEmail(email);
        return response.success(HttpStatus.OK, "사용 가능한 아이디(이메일)입니다.");
    }


    /**
     * @author 박수현
     * @date 2023-07-11
     * @description : 회원가입
     * @return
     */

    @PostMapping("/signup")
    @Operation(
            summary = "회원가입 API",
            description = "유효성 검사를 통해 회원가입을 진행한다. \n" +
                    "또, Role은 BREEDER 혹은 ADOPTER 혹은 ADMIN값으로만 요청해야한다. "
    )
    //@RequestBody - JSON으로 던진 데이터를 받기 위한 어노테이션!
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = MemberSchema.MemberSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    public ResponseEntity<?> signupMember(
            @RequestBody @Valid MemberDto.MemberDtoRequest memberDto,
            BindingResult bindingResult) {
        MemberDto.MemberDtoReponse reponseDto = null;
        log.info("memberDto : " + memberDto.toString());
        try {
            if (!memberDto.isEmailChecked() || !memberDto.isNicknameChecked()) {
                return response.fail(HttpStatus.BAD_REQUEST, "이메일(아이디) 또는 닉네임 중복확인이 필요합니다.");
            }
            if (!memberDto.isPhoneNumberChecked()) {
                return response.fail(HttpStatus.BAD_REQUEST, "핸드폰 인증확인이 필요합니다.");
            }
            // 유효성 검사를 통과하지 못한 경우 바로 에러 메시지 반환
            if (bindingResult.hasErrors()) {
                Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
                return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
            }
            if (!memberDto.getPassword().equals(memberDto.getConfirmPassword())) {
                return response.fail(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
            }
            reponseDto = memberService.signupMember(memberDto);
            return response.success(HttpStatus.OK, reponseDto);
        } catch (Exception ex) {
            return response.error(ex.getMessage());
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-11
     * @description : JWT로그인, RefreshToken정보는 Redis에 저장하는 방식으로 수정
     * @return
     */
    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호을 통해 로그인 진행"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일과 비밀번호을 통해 로그인을 할 수 있다.",
                    content = @Content(schema = @Schema(implementation = LoginSchema.LoginSchema200.class))
            )
    })
    public ResponseEntity<?> login(@RequestBody LoginDto.LoginRequestDto loginDto) {
        // 유저 존재 확인
        Member member = memberService.findMember(loginDto);
        if(member != null){
            LoginDto.LoginResponseDto jwtTokenResponseDto = memberService.checkPassword(member, loginDto);
            if(jwtTokenResponseDto != null){
                return response.success(HttpStatus.OK, jwtTokenResponseDto);
            } else {
                return response.fail(HttpStatus.OK, "잘못된 비밀번호입니다.");
            }
        } else {
            return response.fail(HttpStatus.OK, "해당 회원을 찾을 수 없습니다.");
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 로그아웃 시 redis에 저장된 정보 삭제
     * @return
     */

    @GetMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "Access Token을 헤더에서 추출하여 로그아웃"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            )
    })
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String accessToken = jwtTokenProvider.getAccessToken(request);
        memberService.logout(accessToken);

        return response.success(HttpStatus.OK, "로그아웃이 성공적으로 이루어졌습니다.");
    }

    /**
     * @author 박수현
     * @date 2023-08-24
     * @description : 아이디(이메일) 찾기
     * @return
     */
    @PostMapping("/email/find")
    @Operation(
            summary = "아이디(이메일)찾기",
            description = "닉네임을 기반으로 아이디(이메일)찾기"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "아이디(이메일)찾기 성공",
                    content = @Content(schema = @Schema(implementation = MemberSchema.EmailFindSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            )
    })
    public ResponseEntity<?> findEmail(
            @Valid @RequestBody MemberDto.IdFindRequestDto request,
            BindingResult bindingResult) {

        if (!request.isPhoneNumberChecked()) {
            return response.fail(HttpStatus.BAD_REQUEST, "핸드폰 인증확인이 필요합니다.");
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
            return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
        }

        MemberDto.IdFindResponseDto responseDto = memberService.findEmail(request.getNickname());

        if (responseDto == null) {
            return response.fail(HttpStatus.OK, "회원이 존재하지 않습니다.");
        }

        return response.success(HttpStatus.OK, responseDto);
    }

    /**
     * @author 박수현
     * @date 2023-08-24
     * @description : 비밀번호 찾기 페이지의 사용자 인증
     * @return
     */
    @PostMapping("/pwd/reset")
    @Operation(
            summary = "비밀번호 찾기",
            description = "비밀번호 찾기 페이지의 사용자 인증"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 인증 성공",
                    content = @Content(schema = @Schema(implementation = MemberSchema.EmailFindSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            )
    })
    public ResponseEntity<?> findPwd(
            @Valid @RequestBody MemberDto.PwdFindRequestDto request,
            BindingResult bindingResult) {

        if (!request.isPhoneNumberChecked()) {
            return response.fail(HttpStatus.BAD_REQUEST, "핸드폰 인증확인이 필요합니다.");
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = ErrorResponse.createErrorMessage(bindingResult);
            return response.fail(HttpStatus.BAD_REQUEST, errorMessage);
        }

        Member member = memberRepository.findByEmailAndPhoneNumber(request.getEmail(), request.getPhoneNumber());

        if (member == null) {
            return response.fail(HttpStatus.NOT_FOUND,"회원을 찾을 수 없습니다.");
        }

        return response.success(HttpStatus.OK, "인증이 성공적으로 이루어졌습니다.");
    }


    @PostMapping("/withdraw")
    @Operation(
            summary = "회원탈퇴",
            description = "회원탈퇴"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원탈퇴 성공",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인을 해야만 회원탈퇴 가능",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            ),
    })
    public ResponseEntity<?> withdrawUser(Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        memberService.withdrawMember(principal);
        return response.success(HttpStatus.OK, "회원탈퇴가 성공적으로 이루어졌습니다.");
    }
}

