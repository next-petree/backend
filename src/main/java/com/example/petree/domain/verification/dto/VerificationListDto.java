package com.example.petree.domain.verification.dto;

import com.example.petree.domain.verification.domain.Certification;
import com.example.petree.domain.verification.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class VerificationListDto {
    @Schema(description = "브리더 자격 요청 자격증 종류",example = "PET_BEHAVIOR_CORRECTOR")
    private Certification certification;
    @Schema(description = "브리더 인증 진행 상태",example = "WAITING")
    private Status status;
    @Schema(description = "인증 요청 브리더 이메일",example = "test1@test.com")
    private String breederEmail;
    @Schema(description = "인증 요청 브리더 닉네임",example = "test1")
    private String breederNickname;
    @Schema(description = "인증 요청 제출 날짜",example = "2023-08-08")
    private LocalDate submitDate;
    @Schema(description = "제출 파일",example ="abc.pdf")
    private List<String> fileUrls;

}
