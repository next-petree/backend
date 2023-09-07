package com.example.petree.domain.main_breed.dto;

import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.domain.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class MainBreedDto {

    @Getter
    @NoArgsConstructor
    @ToString
    public static class MainBreedDtoRequest {

        @Schema(description = "견종 식별자 리스트", example = "[1, 2, 3]")
        private List<Long> dogTypeId;

        @JsonCreator
        public MainBreedDtoRequest(@JsonProperty("dogTypeId") List<Long> dogTypeId) {
            this.dogTypeId = dogTypeId;
        }
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @Builder
    public static class MainBreedDtoResponse {

        @Schema(description = "견종 식별자", example = "1")
        private Long id;
        @Schema(description = "견종명", example = "골든 리트리버")
        private String name;
        @Schema(description = "썸네일 이미지 url", example = "https://~")
        private String imgUrl;

        public MainBreedDtoResponse(DogType dogType) {
            this.id = dogType.getId();
            this.name = dogType.getName();
            this.imgUrl = dogType.getImgUrl();
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-29
     * @description : entity -> DTO로 변환
     * @return
     */
    public static MainBreedDto.MainBreedDtoResponse createMainBreedDto(MainBreed mainBreed) {
        return new MainBreedDto.MainBreedDtoResponse(
                mainBreed.getDogType()
        );
    }

}
