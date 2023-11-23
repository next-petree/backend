package com.example.petree.domain.adopter.schema;

import com.example.petree.domain.adopter.dto.AdopterDetailDto;
import com.example.petree.domain.adopter.dto.ReviewDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * packageName    : com.example.petree.domain.adopter.schema
 * fileName       : ReviewSchema
 * author         : 박수현
 * date           : 2023-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@Data
public class ReviewSchema {

    @Data
    public static class ReviewSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private ReviewDto.ReviewResponseDto data;
    }

    @Data
    public static class ReviewsSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        private Page<ReviewDto.ReviewResponseDto> data;
    }


}
