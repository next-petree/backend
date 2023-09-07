package com.example.petree.domain.member.dto;

import com.example.petree.domain.adopter.dto.ResidentialEnvDto;
import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.ProfileImgFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.member.dto
 * fileName       : ProfileDto
 * author         : 박수현
 * date           : 2023-08-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-03        박수현              최초 생성
 */

@Data
@Builder
public class ProfileDto {

    @Data
    @Builder
    public static class PersonalInfoResponseDto{
        @Schema(description = "회원의 PK", example = "1")
        private Long id;
        @Schema(description = "이메일", example = "test1@test.com")
        private String email;
        @Schema(description = "닉네임", example = "test1")
        private String nickname;
        @Schema(description = "전화번호", example = "010-1111-1111")
        private String phoneNumber;
        @Schema(description = "주소", example = "서울시 강남구 도곡로 123")
        private String address1;
        @Schema(description = "상세주소", example = "101호 5층")
        private String address2;
        @Schema(description = "인증여부", example = "true")
        private Boolean verified;

        public static PersonalInfoResponseDto createpersonalInfoResponseDto(Member member) {

            Boolean verified;
            if (member instanceof Breeder) {
                verified = ((Breeder) member).getIsVerified();
            } else if (member instanceof Adopter) {
                verified = ((Adopter) member).getIsVerified();
            } else {
                verified = false;
            }

            return PersonalInfoResponseDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .address1(member.getAddress1())
                    .address2(member.getAddress2())
                    .verified(verified)
                    .build();
        }
    }

    @Data
    @Builder
    public static class PersonalInfoRequestDto{

        @Schema(description = "닉네임 중복확인 여부", example = "true")
        private boolean nicknameChecked;

        @Schema(description = "닉네임", example = "test1")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;

        @Schema(description = "주소", example = "서울시 강남구 도곡로 123")
        @NotBlank(message = "주소는 필수 입력 값입니다.")
        private String address1;
        @Schema(description = "상세주소", example = "101호 5층")
        private String address2;
    }

    @Data
    @Builder
    public static class ProfileImgResponseDto {

        @Schema(description = "프로필이미지 파일의 PK", example = "1")
        private Long id;
        @Schema(description = "S3에 저장되는 파일 경로", example = "s3://<bucket-name>/<file-path>/<file-name>\n")
        private String fileUrl;
    }

    public static ProfileImgResponseDto createProfileImgResponseDTO(ProfileImgFile profileImgFile) {
        return ProfileImgResponseDto.builder()
                .id(profileImgFile.getId())
                .fileUrl(profileImgFile.getFileUrl())
                .build();
    }

    @Data
    @Builder
    public static class PasswordChangeRequestDto{

        @Schema(description = "비밀번호", example = "1234@")
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-={}:\";'<>?,./\\[\\]\\\\|])(?=\\S+$).{4,16}$", message = "비밀번호는 4~16자로 특수문자를 포함하세요.")
        private String currentPassword;
        @Schema(description = "새롭게 등록할 비밀번호", example = "1234@")
        @NotBlank(message = "변경할 비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-={}:\";'<>?,./\\[\\]\\\\|])(?=\\S+$).{4,16}$", message = "비밀번호는 4~16자로 특수문자를 포함하세요.")
        private String newPassword;
        @Schema(description = "비밀번호 확인", example = "1234@")
        @NotBlank(message = "변경할 비밀번호 확인은 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-={}:\";'<>?,./\\[\\]\\\\|])(?=\\S+$).{4,16}$", message = "비밀번호는 4~16자로 특수문자를 포함하세요.")
        private String newPasswordConfirmation;
    }

}
