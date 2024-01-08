package com.example.petree.domain.adopter.dto;

import com.example.petree.domain.adopter.domain.ResidentialEnvImgFile;
import com.example.petree.domain.adopter.domain.SpaceType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.adopter.dto
 * fileName       : ResidentialEnvDto
 * author         : jsc
 * date           : 2023/08/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/21        jsc
 */

public class ResidentialEnvDto {

    @Data
    public static class EnvRequestDto {

        @Schema(description = "삭제한 이미지의 식별자 id. 없으면 빈 배열로 보내주면 됨", example = "[1, 2]")
        private List<Long> deletedImgsId;

    }

//    @Data
//    public static class LivingRoomImgRequestDto{
//        @Schema(description = "추가한 거실 이미지 객체. 없으면 빈 값으로 보내주면 됨")
//        @JsonSerialize(using = ToStringSerializer.class)
//        private MultipartFile livingRoomImg;
//    }
//
//    @Data
//    public static class BathRoomImgRequestDto{
//        @Schema(description = "추가한 화장실 이미지 객체. 없으면 빈 값으로 보내주면 됨")
//        @JsonSerialize(using = ToStringSerializer.class)
//        private MultipartFile bathRoomImg;
//    }
//
//    @Data
//    public static class YardImgRequestDto{
//        @Schema(description = "추가한 마당 이미지 객체. 없으면 빈 값으로 보내주면 됨")
//        @JsonSerialize(using = ToStringSerializer.class)
//        private MultipartFile yardImg;
//    }

    @Getter
    @NoArgsConstructor
    public static class EnvResponseDto {

        @Schema(description = "주거환경 이미지 식별자 id, 저장된 이미지가 없으면 null", example = "1")
        private Long id;
        @Schema(description = "이미지 저장 url, 저장된 이미지가 없으면 null", example = "https://~")
        private String imgUrl;
        @Schema(description = "공간 타입(LIVING_ROOM, BATH_ROOM, YARD)", example = "LIVING_ROOM")
        private SpaceType spaceType;

        public EnvResponseDto(ResidentialEnvImgFile residentialEnvImgFile) {
            this.id = residentialEnvImgFile.getId();
            this.imgUrl = residentialEnvImgFile.getFileUrl();
            this.spaceType = residentialEnvImgFile.getSpaceType();
        }
    }


}
