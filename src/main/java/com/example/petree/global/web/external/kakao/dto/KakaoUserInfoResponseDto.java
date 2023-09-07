package com.example.petree.global.web.external.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.petree.global.web.external.kakao.dto
 * fileName       : KakaoUserInfoResponseDto
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 카카오 서버로부터 정보 받기
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@Data
public class KakaoUserInfoResponseDto {
    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter @Setter
        public static class Profile {

            private String nickname;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

        }

    }
}
