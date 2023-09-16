package com.example.petree.domain.verification.dto;

import com.example.petree.domain.member.domain.ProfileImgFile;
import com.example.petree.domain.member.dto.ProfileDto;
import com.example.petree.domain.verification.domain.Certification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * packageName    : com.example.petree.domain.verification.dto
 * fileName       : VerificationFromResponseDto
 * author         : 박수현
 * date           : 2023-09-16
 * description    : 브리더 인증요청 반환값
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        박수현              최초 생성
 */
@Data
@Builder
public class VerificationFromResponseDto {
    @Schema(description = "프로필이미지 파일의 PK", example = "1")
    private Long id;
    @Schema(description = "자격증 분류 (반려동물종합관리사 OR 반려동물행동교정사)",example = "반려동물종합관리사")
    private Certification certification;
    @Schema(description = "S3에 저장되는 파일 경로", example = "s3://<bucket-name>/<file-path>/<file-name>\n")
    private String fileUrl;

    public static VerificationFromResponseDto createVerificationFromResponseDto(VerificationFormDto verificationFormDto) {
        return VerificationFromResponseDto.builder()
                .id(verificationFormDto.getBreederId())
                .certification(verificationFormDto.getCertification())
                .fileUrl(verificationFormDto.getVerificationFiles().getOriginalFilename()).build();
    }
}
