package com.example.petree.domain.matching.dto;

import com.example.petree.domain.dog.domain.DogImgFile;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.matching.domain.Matching;
import com.example.petree.domain.matching.domain.Pledge;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.matching.dto
 * fileName       : DetailMatchingOfBreederDto
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
public class DetailMatchingOfBreederDto {

    @Schema(description = "강아지 대표 사진 저장 경로", example = "{강아지 사진 저장 경로}")
    private String imgUrl;
    @Schema(description = "강아지 이름", example = "쫑이")
    private String name;
    @Schema(description = "견종명", example = "토이푸들")
    private String breedType;
    @Schema(description = "강아지 성별", example = "MALE")
    private Gender gender;
    @Schema(description = "강아지 생년월일", example = "2023-07-01")
    private LocalDate birthDate;
    @Schema(description = "문항1(분양사유)", example = "")
    private String nurturingEnv;
    @Schema(description = "문항2(마음가짐)", example = "")
    private String parentExp;


    public DetailMatchingOfBreederDto(Matching matching, Pledge pledge){
        /*this.name = matching.getDog().getName();
        this.breedType = matching.getDog().getDogType().getName();
        this.gender = matching.getDog().getGender();
        this.birthDate = matching.getDog().getBirthDate();
        if(matching.getMatchingApproval() == null || !matching.getMatchingApproval().getIsApproved()) {
            this.adopterPhoneNumber = null;
            this.adopterAddress = null;
        }else{
            this.adopterPhoneNumber = matching.getAdopter().getPhoneNumber();
            this.adopterAddress = matching.getAdopter().getAddress1();
        }*/
        this.imgUrl = (matching.getDog().getDogImgFiles() != null && !matching.getDog().getDogImgFiles().isEmpty())
                ? matching.getDog().getDogImgFiles().get(0).getFileUrl() : null;
        this.name = matching.getDog().getName();
        this.breedType = matching.getDog().getDogType().getName();
        this.gender = matching.getDog().getGender();
        this.birthDate = matching.getDog().getBirthDate();

        // Pledge 정보 설정
        if (pledge != null) {
            this.nurturingEnv = pledge.getNurturingEnv();
            this.parentExp = pledge.getParentExp();
        } else {
            this.nurturingEnv = null;
            this.parentExp = null;
        }

    }

}
