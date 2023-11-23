package com.example.petree.domain.matching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.example.petree.domain.matching.dto
 * fileName       : MatchingApprovalDto
 * author         : qkrtn_ulqpbq2
 * date           : 2023-09-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-12        qkrtn_ulqpbq2       최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
public class MatchingApprovalDto {

    @Schema(description = "분양 희망자 전화번호(신청 수락 시에만 노출)", example = "010-1234-1234")
    private String adopterPhoneNumber;
    @Schema(description = "분양 희망자 닉네임(신청 수락 시에만 노출)", example = "경기도 수원시 ~")
    private String adopterAddress;
}
