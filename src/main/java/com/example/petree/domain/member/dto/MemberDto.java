package com.example.petree.domain.member.dto;

import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import com.example.petree.global.config.MapConfig;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MemberDto {

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDtoRequest{
        @Schema(description = "이메일 중복확인 여부", example = "true")
        private boolean emailChecked;
        @Schema(description = "닉네임 중복확인 여부", example = "true")
        private boolean nicknameChecked;
        @Schema(description = "핸드폰 인증여부", example = "true")
        private boolean phoneNumberChecked;
        @Schema(description = "이메일", example = "test1@test.com")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @Schema(description = "닉네임", example = "test1")
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
        @Schema(description = "비밀번호", example = "1234@")
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-={}:\";'<>?,./\\[\\]\\\\|])(?=\\S+$).{4,16}$", message = "비밀번호는 4~16자로 특수문자를 포함하세요.")
        private String password;
        @Schema(description = "비밀번호 확인", example = "1234@")
        @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
        private String confirmPassword;
        @Schema(description = "전화번호", example = "010-1111-1111")
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?!(\\d)\\1+$)\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String phoneNumber;
        @Schema(description = "주소", example = "서울시 강남구 도곡로 123")
        @NotBlank(message = "주소는 필수 입력 값입니다.")
        private String address1;
        @Schema(description = "상세주소", example = "101호 5층")
        private String address2;
        @Schema(description = "역할", example = "BREEDER")
        private Role role;
        @Schema(description = "주력견종 정보", example = "{\"dogTypeId\": [1, 2, 3]}")
        private Optional<MainBreedDto.MainBreedDtoRequest> mainBreedDtoRequest;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    public static class MemberDtoReponse{
        @JsonIgnore
        @Schema(description = "자동 증가값")
        private Long id;
        @Schema(description = "이메일")
        private String email;
        @Schema(description = "닉네임")
        private String nickname;
        @JsonIgnore
        private String password;
        @Schema(description = "전화번호")
        private String phoneNumber;
        @Schema(description = "주소")
        private String address1;
        @Schema(description = "상세주소")
        private String address2;
        @JsonIgnore
        @Schema(description = "위도")
        private Double latitude;
        @JsonIgnore
        @Schema(description = "경도")
        private Double longitude;
        @Schema(description = "역할")
        private Role role;
        @Schema(description = "주력견종 정보")
        private List<MainBreedDto.MainBreedDtoResponse> mainBreedDtoResponseList;

        public static Member toEntity(MemberDto.MemberDtoReponse memberDto) {
            return new Member(memberDto.id,
                    memberDto.email,
                    memberDto.nickname,
                    memberDto.password,
                    memberDto.phoneNumber,
                    memberDto.address1,
                    memberDto.address2,
                    memberDto.latitude,
                    memberDto.longitude,
                    memberDto.role);
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    public static class IdFindRequestDto {
        @Schema(description = "닉네임", example = "test1")
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
        @Schema(description = "핸드폰 인증여부", example = "true")
        private boolean phoneNumberChecked;
    }


    @Getter
    @Setter
    @ToString
    @Builder
    public static class IdFindResponseDto {
        @Schema(description = "이메일", example = "test1@test.com")
        private String email;
    }


    @Getter
    @Setter
    @ToString
    @Builder
    public static class PwdFindRequestDto {
        @Schema(description = "이메일", example = "test1@test.com")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @Schema(description = "핸드폰 인증여부", example = "true")
        private boolean phoneNumberChecked;
        @Schema(description = "전화번호", example = "010-1111-1111")
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?!(\\d)\\1+$)\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String phoneNumber;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithdrawalRequestDto {
        @Schema(description = "비밀번호", example = "1234@")
        private String password;
    }

}
