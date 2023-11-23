package com.example.petree.domain.dog.dto;

import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.DogImgFile;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.dog.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.petree.domain.dog.dto
 * fileName       : SimpleDogDto
 * author         : jsc
 * date           : 2023/07/09
 * description    : 강아지 기본 정보에 대한 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/04        정세창               init
 * 2023/07/09        정세창            이미지 1개에 대한 url 추가
 */

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDogDto {

    @Schema(description = "강아지 식별 id", example = "1")
    private Long id;
    @Schema(description = "강아지 이름", example = "쫑이")
    private String name;
    @Schema(description = "견종명", example = "토이푸들")
    private String type;
    @Schema(description = "성별", example = "MALE")
    private Gender gender;
    @Schema(description = "입양진행상태(AVAILABLE, UNDERWAY, DONE)", example = "AVAILABLE")
    private Status status;
    @Schema(description = "생년월일", example = "2023-07-01")
    private LocalDate birthDate;
    @Schema(description = "강아지 대표 사진 저장 경로", example = "{강아지 사진 저장 경로}")
    private String imgUrl;
    @Schema(description = "해당 강아지 소유 브리더 닉네임", example = "푸들조아")
    private String breederNickName;
    @Schema(description = "해당 강아지 소유 브리더 인증 여부(true/false)", example = "true")
    private Boolean isBreederVerified;

    public SimpleDogDto(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.type = dog.getDogType().getName();
        this.gender = dog.getGender();
        this.status = dog.getStatus();
        this.birthDate = dog.getBirthDate();
        this.imgUrl = (dog.getDogImgFiles() != null && !dog.getDogImgFiles().isEmpty())
                ? dog.getDogImgFiles().get(0).getFileUrl() : null;
        this.breederNickName = dog.getBreeder().getNickname();
        this.isBreederVerified = dog.getBreeder().getIsVerified();
    }

}

