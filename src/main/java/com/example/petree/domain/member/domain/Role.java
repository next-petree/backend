package com.example.petree.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : com.example.petree.domain.member.domain
 * fileName       : Role
 * author         : 박수현
 * date           : 2023-06-30
 * description    : Role을 Class -> Enum으로 변환
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-30        박수현       최초 생성
 */

@Getter
@ToString
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Role {
    @Schema(description = "브리더", example = "BREEDER")
    BREEDER("ROLE_BREEDER", "BREEDER"),
    @Schema(description = "입양희망자", example = "ADOPTER")
    ADOPTER("ROLE_ADOPTER", "ADOPTER"),
    @Schema(description = "관리자", example = "ADMIN")
    ADMIN("ROLE_ADMIN", "ADMIN");

    private final String key;
    private final String title;

    public static boolean contains(Role role) {
        for (Role r : values()) {
            if (r == role) {
                return true;
            }
        }
        return false;
    }
}
