package com.example.petree.domain.matching.dto;

import com.example.petree.domain.dog.domain.Status;
import com.example.petree.domain.matching.domain.Matching;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    @Schema(description = "브리더 닉네임", example = "김철수")
    private String breederNickname;
    @Schema(description = "매칭 신청 날짜", example = "2023-07-01")
    private LocalDate submitDate;
    @Schema(description = "강아지 이름", example = "쫑이")
    private String dogName;
    @Schema(description = "견종명", example = "토이푸들")
    private String dogTypeName;
    @Schema(description = "입양진행 상태(AVAILABLE, UNDERWAY, DONE)", example = "AVAILABLE")
    private Status status;
    @Schema(description = "신청 처리 여부. null은 신청 처리 안함 / true는 해당 요청 승인함 / false는 해당 요청 거절함", example = "true")
    private Boolean isProcessed;
    @Schema(description = "브리더가 해당 보유견종을 '입양완료' 상태로 변경했다면 true. '후기쓰기 버튼을 활성화하는데 사용", example = "true")
    private Boolean isDone;
    @Schema(description = "강아지 식별 id", example = "1")
    private Long dogId;

    public SimpleMatchingOfAdopterDto(Matching matching) {
        this.matchingId = matching.getId();
        this.breederNickname = matching.getBreeder().getNickname();
        this.submitDate = matching.getSubmitDate();
        this.dogName = matching.getDog().getName();
        this.dogTypeName = matching.getDog().getDogType().getName();
        this.status = matching.getDog().getStatus();
        this.isProcessed = matching.getMatchingApproval() != null ?
                matching.getMatchingApproval().getIsApproved() : null;
        this.isDone = matching.getDog().getStatus() == Status.DONE;
        this.dogId = matching.getDog().getId();
    }
}
