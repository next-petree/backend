package com.example.petree.domain.matching.dto;

import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.matching.domain.Matching;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.matching.dto
 * fileName       : DetailMatchingOfAdopterDto
 * author         : 정세창
 * date           : 2023/07/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/13         정세창              init
 */
@Getter
@NoArgsConstructor
public class DetailMatchingOfAdopterDto {
    @Schema(description = "강아지 이름", example = "쫑이")
    private String name;
    @Schema(description = "견종명", example = "토이푸들")
    private String breedType;
    @Schema(description = "강아지 성별", example = "MALE")
    private Gender gender;
    @Schema(description = "강아지 생년월일", example = "2023-07-01")
    private LocalDate birthDate;
    @ArraySchema(schema = @Schema(description = "양육환경 이미지 저장 url 리스트", example = "{이미지 저장 url}"))
    private List<String> matchingImgs;
    @Schema(description = "브리더 닉네임(신청 수락 시에만 노출)", example = "김철수")
    private String breederNickname;
    @Schema(description = "브리더 전화번호(신청 수락 시에만 노출)", example = "010-1234-1234")
    private String breederPhoneNumber;
    @Schema(description = "브리더 닉네임(신청 수락 시에만 노출)", example = "경기도 수원시 ~")
    private String breederAddress;

    public DetailMatchingOfAdopterDto(Matching matching) {
        this.name = matching.getDog().getName();
        this.breedType = matching.getDog().getDogType().getName();
        this.gender = matching.getDog().getGender();
        this.birthDate = matching.getDog().getBirthDate();
        if (matching.getMatchingApproval() == null || !matching.getMatchingApproval().getIsApproved()) {
            this.breederNickname = null;
            this.breederPhoneNumber = null;
            this.breederAddress = null;
        } else {
            this.breederNickname = matching.getBreeder().getNickname();
            this.breederPhoneNumber = matching.getBreeder().getPhoneNumber();
            this.breederAddress = matching.getBreeder().getAddress1();
        }
    }
}
