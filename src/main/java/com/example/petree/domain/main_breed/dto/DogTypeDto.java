package com.example.petree.domain.main_breed.dto;

import com.example.petree.domain.main_breed.domain.DogType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : com.example.petree.domain.main_breed.dto
 * fileName       : DogTypeDto
 * author         : 박수현
 * date           : 2023-08-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-14        박수현              최초 생성
 */

@AllArgsConstructor
@ToString
@Getter
public class DogTypeDto {

    @Schema(description = "견종 식별자", example = "1")
    private Long id;
    @Schema(description = "견종 이름", example = "골든 리트리버")
    private String name;
    @Schema(description = "견종 사진 경로", example = "{견종 사진 저장 경로}")
    private String imgUrl;

    public static DogTypeDto createDogTypeDto(DogType dogType) {
        return new DogTypeDto(dogType.getId(), dogType.getName(), dogType.getImgUrl());
    }
}
