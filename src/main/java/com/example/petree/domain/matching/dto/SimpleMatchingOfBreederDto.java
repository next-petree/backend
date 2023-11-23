package com.example.petree.domain.matching.dto;

import com.example.petree.domain.matching.domain.Matching;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * packageName    : com.example.petree.domain.matching.dto
 * fileName       : SimpleMatchingOfBreederDto
 * author         : 정세창
 * date           : 2023/07/10
 * description    : 브리더가 입양신청(매칭) 내역 조회 시의 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/10        jsc
 */
@Getter
@NoArgsConstructor
public class SimpleMatchingOfBreederDto {

    @Schema(description = "매칭 식별자 id", example = "1")
    private Long matchingId;
    @Schema(description = "분양 희망자 닉네임", example = "김철수")
    private String adopterNickname;
    @Schema(description = "매칭 신청 날짜", example = "2023-07-01")
    private LocalDate submitDate;
    @Schema(description = "강아지 이름", example = "쫑이")
    private String dogName;
    @Schema(description = "강아지 견종명", example = "말티즈")
    private String dogTypeName;
    @Schema(description = "신청 처리 여부. null은 신청 처리 안함 / true는 해당 요청 승인함 / false는 해당 요청 거절함", example = "true")
    private Boolean isProcessed;

    public SimpleMatchingOfBreederDto(Matching matching) {
        this.matchingId = matching.getId();
        this.adopterNickname = matching.getAdopter().getNickname();
        this.submitDate = matching.getSubmitDate();
        this.dogName = matching.getDog().getName();
        this.dogTypeName = matching.getDog().getDogType().getName();
        this.isProcessed = matching.getMatchingApproval() != null ?
                matching.getMatchingApproval().getIsApproved() : null;
    }
}
