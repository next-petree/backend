package com.example.petree.domain.adopter.dto;

import com.example.petree.domain.adopter.domain.Review;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.member.domain.ProfileImgFile;
import com.example.petree.domain.member.dto.ProfileDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.adopter.dto
 * fileName       : AdoptionReviewDto
 * author         : 박수현
 * date           : 2023-08-28
 * description    : 분양후기 관련 Dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */
public class ReviewDto {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewRequestDto {
        @Schema(description = "분양완료된 강아지 PK", example = "1")
        private Long dogId;
        @NotBlank(message = "제목은 필수 입력값입니다.")
        @Length(max = 30, message = "제목은 최대 30자까지 입력 가능합니다.")
        @Schema(description = "제목", example = "후기등록해요~")
        private String title;
        @NotBlank(message = "내용은 필수 입력값입니다.")
        @Length(max = 2000, message = "내용은 최대 2000자까지 입력 가능합니다.")
        @Schema(description = "내용", example = "정성스럽게 키우곘습니다!")
        private String content;
        @NotEmpty(message = "이미지는 필수 입력값입니다.")
        @Schema(description = "분양 후기 이미지 리스트")
        @JsonSerialize(using = ToStringSerializer.class)
        private List<MultipartFile> reviewImgFiles;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewUpdateRequestDto {
        @NotBlank(message = "제목은 필수 입력값입니다.")
        @Length(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
        @Schema(description = "제목", example = "후기등록해요~")
        private String title;
        @NotBlank(message = "내용은 필수 입력값입니다.")
        @Length(max = 3000, message = "내용은 최대 3000자까지 입력 가능합니다.")
        @Schema(description = "내용", example = "정성스럽게 키우곘습니다!")
        private String content;
        @NotBlank(message = "이미지 삭제 여부는 필수 입력값입니다.")
        @Schema(description = "이미지 삭제 여부")
        private boolean deleteImages;
        @NotEmpty(message = "이미지는 필수 입력값입니다.")
        @Schema(description = "분양 후기 이미지 리스트")
        @JsonSerialize(using = ToStringSerializer.class)
        private List<MultipartFile> reviewImgFiles;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewResponseDto {
        @Schema(description = "분양후기 PK", example = "1")
        private Long id;
        @Schema(description = "강아지 PK", example = "1")
        private Long dogId;
        @Schema(description = "강아지 이름", example = "쫑이")
        private String name;
        @Schema(description = "견종 정보", example = "레브라도 리트리버")
        private String dogTypeName;
        @Schema(description = "성별", example = "FEMALE")
        private Gender gender;
        @Schema(description = "등록날짜", example = "2021-03-21")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate writeDate;
        @Schema(description = "제목", example = "후기등록해요~")
        private String title;
        @Schema(description = "내용", example = "정성스럽게 키우곘습니다!")
        private String content;
        @Schema(description = "분양후기 이미지 리스트")
        private List<ReviewImgResponseDto> reviewImgId;

        public static ReviewResponseDto createReviewResponseDto(Review review) {
            List<ReviewImgResponseDto> imgResponseDtos = review.getImages().stream()
                    .map(imgFile -> ReviewImgResponseDto.builder()
                            .id(imgFile.getId())
                            .fileUrl(imgFile.getFileUrl())
                            .build())
                    .collect(Collectors.toList());

            return ReviewResponseDto.builder()
                    .id(review.getId())
                    .dogId(review.getDog().getId())
                    .name(review.getDog().getName())
                    .dogTypeName(review.getDog().getDogType().getName())
                    .gender(review.getDog().getGender())
                    .writeDate(review.getWriteDate())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .reviewImgId(imgResponseDtos)
                    .build();
        }
    }

    @Data
    @Builder
    public static class ReviewImgResponseDto {

        @Schema(description = "분양후기이미지 파일의 PK", example = "1")
        private Long id;
        @Schema(description = "S3에 저장되는 파일 경로", example = "s3://<bucket-name>/<file-path>/<file-name>\n")
        private String fileUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListResponseDto {
        @Schema(description = "분양후기 PK", example = "1")
        private Long id;
        @Schema(description = "강아지 PK", example = "1")
        private Long dogId;
        @Schema(description = "강아지 이름", example = "쫑이")
        private String name;
        @Schema(description = "견종 정보", example = "레브라도 리트리버")
        private String dogTypeName;
        @Schema(description = "성별", example = "FEMALE")
        private Gender gender;
        @Schema(description = "등록날짜", example = "2021-03-21")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate writeDate;
        @Schema(description = "제목", example = "후기등록해요~")
        private String title;
        @Schema(description = "내용", example = "정성스럽게 키우곘습니다!")
        private String content;
        @Schema(description = "분양후기 이미지 대표 사진 저장 경로", example = "{분양후기이미지 사진 저장 경로}")
        private String imgUrl;

        public static ReviewListResponseDto createReviewListResponseDto(Review review) {

            return ReviewListResponseDto.builder()
                    .id(review.getId())
                    .dogId(review.getDog().getId())
                    .name(review.getDog().getName())
                    .dogTypeName(review.getDog().getDogType().getName())
                    .gender(review.getDog().getGender())
                    .writeDate(review.getWriteDate())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .imgUrl(review.getImages().get(0).getFileUrl())
                    .build();
        }
    }
}
