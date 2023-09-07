package com.example.petree.domain.matching.dto;

import com.example.petree.domain.dog.domain.Status;
import com.example.petree.domain.matching.domain.Matching;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * packageName    : com.example.petree.domain.matching.dto
 * fileName       : SimpleMatchingOfAdopterDto
 * author         : 정세창
 * date           : 2023/07/10
 * description    : 입양희망자가 신청한 매칭 신청 리스트 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/10        정세창               init
 */
@Getter
@NoArgsConstructor
public class SimpleMatchingOfAdopterDto {

    @Schema(description = "매칭 식별자 id", example = "1")
    private Long matchingId;
    @Schema(description = "브리더 이메일", example = "abc123@abc.com")
    private String breederEmail;
    @Schema(description = "브리더 닉네임", example = "김철수")
    private String breederNickname;
    @Schema(description = "매칭 신청 날짜", example = "2023-07-01")
    private LocalDate submitDate;
    @Schema(description = "견종명", example = "토이푸들")
    private String breedType;
    @Schema(description = "입양진행 상태(AVAILABLE, UNDERWAY, DONE)", example = "AVAILABLE")
    private Status status;
    @Schema(description = "입양 신청 처리 전이면 null, 처리 후에는 승인/반려 결과에 따라 true/false", example = "true")
    private Boolean approvalResult;

    public SimpleMatchingOfAdopterDto(Matching matching) {
        this.matchingId = matching.getId();
        this.breederEmail = matching.getBreeder().getEmail();
        this.breederNickname = matching.getBreeder().getNickname();
        this.submitDate = matching.getSubmitDate();
        this.breedType = matching.getDog().getDogType().getName();
        this.status = matching.getDog().getStatus();
        this.approvalResult = (matching.getMatchingApproval() != null)
                ? matching.getMatchingApproval().getIsApproved() : null;
    }
}
