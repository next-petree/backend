package com.example.petree.domain.matching.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "브리더 정보는 필수 입력 값입니다.")
    private Long breederId;

    @NotNull(message = "강아지 정보는 필수 입력 값입니다.")
    @Schema(description = "강아지 식별자 id", example = "1")
    private Long dogId;

    @NotBlank(message = "문항1은 필수 입력 값입니다.")
    @Schema(description = "문항 1", example = "잘 키우겠습니다 ~")
    @Size(max = 2000, message = "문항1의 최대 글자수는 2000자입니다.")
    private String pledgeContent1;

    @NotBlank(message = "문항2는 필수 입력 값입니다.")
    @Schema(description = "문항 2", example = "잘 키우겠습니다 ~")
    @Size(max = 2000, message = "문항2의 최대 글자수는 2000자입니다.")
    private String pledgeContent2;

}
