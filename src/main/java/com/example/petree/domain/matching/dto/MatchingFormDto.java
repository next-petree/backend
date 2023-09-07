package com.example.petree.domain.matching.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.matching.dto
 * fileName       : MatchingFormDto
 * author         : jsc
 * date           : 2023/07/09
 * description    : 매칭 추가 요청 시 받는 form에 대한 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/09          정세창             init
 */
@Data
public class MatchingFormDto {

    @Schema(description = "브리더 식별자 id", example = "1")
    private Long breederId;
    @Schema(description = "강아지 식별자 id", example = "1")
    private Long dogId;
    @Schema(description = "마음가짐 문항 1", example = "잘 키우겠습니다 ~")
    private String pledgeContent1;
    @Schema(description = "마음가짐 문항 2", example = "잘 키우겠습니다 ~")
    private String pledgeContent2;

}
