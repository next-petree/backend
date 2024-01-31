package com.example.petree.domain.dog.dto;

import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.DogImgFile;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.dog.domain.Status;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Data
@NoArgsConstructor
@Slf4j
@Builder //수현 추가
@AllArgsConstructor // 수현 추가
public class PossessionDogDto {

    @Schema(description = "강아지의 PK", example = "4")
    private Long id; // 수현 추가

    @Schema(description = "견종", example = "말티푸")
    private String dogType;

    @Schema(description = "보유 견종 성별", example = "FEMALE")
    private Gender gender;

    @Schema(description = "보유 견종 출생일", example = "2021-03-21")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Schema(description = "보유 견종 이름", example = "이땅콩")
    private String name;

    @Schema(description = "보유 견종 특징", example = "3.7kg의 소형견")
    private String management;

    @Schema(description = "보유 견종 입양 진행 상태",example = "AVAILABLE")
    private Status status;

    @Schema(description = "보유견종 이미지 리스트",example = "{https://petree-bucket.s3.ap-northeast-2.amazonaws.com/dog-img/3eaa46ee-5187-4cb9-9195-1b6231709a83.jpg}")
    private List<String> dogImgUrl;


    public PossessionDogDto(Dog dog) {
        this.id=dog.getId();
        this.dogType=dog.getDogType().getName();
        this.gender=dog.getGender();
        this.birthDate=dog.getBirthDate();
        this.name=dog.getName();
        this.management=dog.getManagement();
        this.dogImgUrl=dog.getDogImgFiles()
                .stream()
                .map(DogImgFile::getFileUrl)
                .collect(Collectors.toList());
    }

    @Getter
    @ToString
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDogDto {

        @Schema(description = "보유 견종 성별", example = "FEMALE")
        @NotNull(message = " 강아지의 성별은 필수 입력 값입니다.")
        private Gender gender;

        @Schema(description = "보유 견종 출생일", example = "2021-03-21")
        @NotNull(message = "강아지의 출생일은 필수 입력 값입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        @Schema(description = "보유 견종 이름", example = "이땅콩")
        @NotBlank(message = "강아지의 이름은 필수 입력 값입니다.")
        private String name;

        @Schema(description = "보유 견종 특징", example = "3.7kg의 소형견")
        @NotBlank(message = "강아지의 상세내용은 필수 입력 값입니다.")
        @Size(max = 2000, message = "특징은 2000자 이내로 작성이 가능합니다.")
        private String management;

        @Schema(description = "보유 견종의 이미지를 입력받는 리스트")
        @NotNull(message = "강아지의 이미지는 필수 입력 값입니다.")
        @JsonSerialize(using = ToStringSerializer.class)
        private List<MultipartFile> dogImgFiles;

        public CreateDogDto(Dog dog) {
            this.gender=dog.getGender();
            this.birthDate=dog.getBirthDate();
            this.name=dog.getName();
            this.management=dog.getManagement();
        }

        public List<MultipartFile> getDogImgFiles() {

            return dogImgFiles;
        };
    }
    @Getter
    @ToString
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateDogDto{

        @Schema(description = "보유 견종 성별", example = "FEMALE")
        @NotNull(message = " 강아지의 성별은 필수 입력 값입니다.")
        private Gender gender;

        @Schema(description = "보유 견종 출생일", example = "2021-03-21")
        @NotNull(message = "강아지의 출생일은 필수 입력 값입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        @Schema(description = "보유 견종 이름", example = "이땅콩")
        @NotBlank(message = "강아지의 이름은 필수 입력 값입니다.")
        private String name;

        @Schema(description = "보유 견종 특징", example = "3.7kg의 소형견")
        @NotBlank(message = "강아지의 상세내용은 필수 입력 값입니다.")
        @Size(max = 2000, message = "특징은 2000자 이내로 작성이 가능합니다.")
        private String management;

        @Schema(description = "보유 견종 입양 진행 상태",example = "AVAILABLE")
        @NotBlank(message = "보유 견종 입양 진행 상태는 필수 입력 값입니다.")
        private Status status;

        @Schema(description = "삭제할 이미지 파일 ID값")
        @NotBlank(message = "삭제할 이미지 파일 ID값은 필수 입력 값입니다.")
        private List<String> imgIdToDelete;

        @Schema(description = "이미지 삭제 여부 ", example = "false")
        @NotNull(message = "이미지 삭제 여부는 필수 입력 값입니다.")
        private boolean isDeleteImages;

        @Schema(description = "보유 견종의 이미지를 입력받는 리스트")
        @NotBlank(message = "보유 견종의 이미지는 필수 입력 값입니다.")
        @JsonSerialize(using = ToStringSerializer.class)
        private List<MultipartFile> dogImgFiles;


        public UpdateDogDto(Dog dog) {
            this.gender=dog.getGender();
            this.birthDate=dog.getBirthDate();
            this.name=dog.getName();
            this.management=dog.getManagement();
            this.status =dog.getStatus();
        }

        public void patch(Dog dog) {

            if (this.getGender() == null) {
                this.gender = dog.getGender();
            }
            if (this.getBirthDate() == null) {
                this.birthDate = dog.getBirthDate();
            }
            if (this.getName() == null) {
                this.name = dog.getName();
            }
            if (this.getManagement() == null) {
                this.management =dog.getManagement();
            }
            if(this.getStatus() == null){
                this.status = dog.getStatus();
            }

        }



    }


    /**
     * packageName    : com.example.petree.global
     * fileName       : TemplateController
     * author         : 이지수
     * date           : 2023-07-05
     * description    : 입양가능 반려견 AdoptableDogDto
     * ===========================================================
     * DATE              AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2023-07-05       이지수      최초 생성
     * 2023-07-16       이지수     null값으로 입력되는 것 수정
     * 2023-07-31       이지수     swqgger 적용, 수정 후 LocalDate null로 반환됨
     * 2021-08-03       이지수     LocalDate값 해결
     * 2021-08-21       이지수     불필요한 내용 삭제
     * 2021-08-28       이지수     status자동으로 생성하게 수정 -> 현재 dto에서 필요 없으므로 삭제
     * 2023-08-29       이지수     create, update 분리
     * 2023-08-29       이지수     강아지 단건 조회 시 견종 추가
     * 2023-08-31       이지수     보유 견종 입양 진행 상태 추가 (수정 시)
     * */

}
