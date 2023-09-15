package com.example.petree.domain.dog.dto;

import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.DogImgFile;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.dog.domain.Status;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.dog.dto
 * fileName       : DetailDogDto
 * author         : jsc
 * date           : 2023/07/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/06        정세창             파일 생성
 */
@Getter
@NoArgsConstructor
public class DetailDogDto {
    @Schema(description = "강아지 식별자 id", example = "1")
    private Long id;
    @Schema(description = "강아지 이름", example = "쫑이")
    private String name;
    @Schema(description = "견종명", example = "토이푸들")
    private String type;
    @Schema(description = "성별", example = "MALE")
    private Gender gender;
    @Schema(description = "생년월일", example = "2023-07-01")
    private LocalDate birthDate;
    @Schema(description = "강아지 소개", example = "착하고 순해요. 그리고 ~")
    private String management;
    @Schema(description = "브리더 식별자 id", example = "1")
    private Long breederId;
    @Schema(description = "해당 강아지 소유 브리더 닉네임", example = "푸들조아")
    private String breederNickName;
    @Schema(description = "해당 강아지 소유 브리더 인증 여부(true/false)", example = "true")
    private Boolean isBreederVerified;
    @Schema(description = "보유 견종 입양 진행 상태",example = "AVAILABLE")
    private Status status;
    // TODO: 브리더 팔로우 여부 추후에 추가
    @ArraySchema(schema = @Schema(description = "강아지 이미지 저장 url 리스트", example = "{이미지 저장 url}"))
    private List<String> imagesUrl;

    public DetailDogDto(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.type = dog.getDogType().getName();
        this.gender = dog.getGender();
        this.birthDate = dog.getBirthDate();
        this.management = dog.getManagement();
        this.breederId = dog.getBreeder().getId();
        this.breederNickName = dog.getBreeder().getNickname();
        this.isBreederVerified = dog.getBreeder().getIsVerified();
        this.status = dog.getStatus();
        this.imagesUrl = dog.getDogImgFiles()
                .stream()
                .map(DogImgFile::getFileUrl)
                .collect(Collectors.toList());

    }

}
