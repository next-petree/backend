package com.example.petree.global;

import com.example.petree.domain.dog.dto.SimpleDogDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * packageName    : com.example.petree.global
 * fileName       : ResponseSchema
 * author         : jsc
 * date           : 2023/07/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/25        jsc
 */
@Data
public class ResponseSchema {
    protected JsendStatus status;
    private Object data;

    @Data
    public static class ResponseSchema200 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        private String data;
    }

    @Data
    public static class ResponseSchema404 {
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        private String data;
    }

    @Data
    public static class ResponseSchema400S {
        @Schema(example = "FAIL")
        private JsendStatus status;
        private String data;
    }

    @Data
    public static class ResponseSchema400M {
        @Schema(example = "FAIL")
        private JsendStatus status;
        private Map<String, String> data;
    }

    @Data
    public static class ResponseSchema500 {
        @Schema(example = "ERROR")
        private JsendStatus status;
        private String data;
    }

    @Data
    public static class ResponseSchema401 {
        @Schema(example = "FAIL")
        private JsendStatus status;
        @Schema(example = "해당 회원을 찾을 수 없습니다.")
        private String data;
    }

}
