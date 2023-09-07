package com.example.petree.domain.breeder.dto;

import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.breeder.dto
 * fileName       : BreederDetailDto
 * author         : 박수현
 * date           : 2023-08-27
 * description    : 제3자가 보는 브리더프로필 조회 반환 Dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-27        박수현              최초 생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreederDetailDto {

    @Schema(description = "회원의 PK", example = "1")
    private Long id;
    @Schema(description = "닉네임", example = "test1")
    private String nickname;
    @Schema(description = "주소", example = "서울시 강남구 도곡로 123")
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address1;
    @Schema(description = "인증여부", example = "true")
    private Boolean verified;
    @Schema(description = "자기소개", example = "안녕하세요. 홍길동입니다.")
    private String selfIntroduction;
    @Schema(description = "주력견종 목록")
    private List<MainBreedDto.MainBreedDtoResponse> mainBreedDtoResponseList;
    @Schema(description = "프로필 사진 저장 경로", example = "{프로필 사진 저장 경로}")
    private String profileImgUrl;
    @Schema(description = "보유견종 목록")
    private List<PossessionDogDto> possessionDogDtos;
}
