package com.example.petree.domain.breeder.dto;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.domain.BreederProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.breeder.dto
 * fileName       : BreederDto
 * author         : 박수현
 * date           : 2023-07-04
 * description    : 로그인한 사용자와 로그인하지 않은 사용자에 따라 BreederDto를 생성한다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-04        박수현              최초 생성
 */
@Data
@AllArgsConstructor
public class BreederDto implements Serializable {

    private static final long serialVersionUID = 123456789L;

    @Schema(description = "브리더회원의 PK", example = "1")
    private Long id;
    @Schema(description = "이메일", example = "test1@test.com")
    private String email;
    @Schema(description = "닉네임", example = "test1")
    private String nickname;
    @Schema(description = "전화번호", example = "010-1111-1111")
    private String phoneNumber;
    @Schema(description = "주소", example = "서울시 강남구")
    private String address1;
    @Schema(description = "인증여부", example = "true")
    private Boolean verified;
    @Schema(description = "거리", example = "173.9")
    private Double distance;
    @Schema(description = "주력견종 목록", example = "[\"레브라도\", \"골든 리트리버\"]")
    private List<String> types;
    @Schema(description = "브리더 프로필 사진 저장 경로", example = "{브리더 프로필 사진 저장 경로}")
    private String profileImagUrl;


    public static BreederDto createBreederDtoVerificated(BreederProjection breederProjection, String profileImageUrl) {
        List<String> types = null;
        if (breederProjection.getTypes() != null) {
            types = Arrays.asList(breederProjection.getTypes().split(", "));
        }
        return new BreederDto(
                breederProjection.getId(),
                breederProjection.getEmail(),
                breederProjection.getNickname(),
                breederProjection.getPhoneNumber(),
                breederProjection.getAddress1(),
                breederProjection.getIsVerified(),
                breederProjection.getDistance(),
                types,
                profileImageUrl
        );
    }

    public static BreederDto createBreederDtoNotVerificated(BreederProjection breederProjection, String profileImageUrl) {
        List<String> types = null;
        if (breederProjection.getTypes() != null) {
            types = Arrays.asList(breederProjection.getTypes().split(", "));
        }
        return new BreederDto(
                breederProjection.getId(),
                breederProjection.getEmail(),
                breederProjection.getNickname(),
                breederProjection.getPhoneNumber(),
                breederProjection.getAddress1(),
                breederProjection.getIsVerified(),
                null,
                types,
                profileImageUrl
        );
    }
}

