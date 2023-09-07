package com.example.petree.global.error;

import com.example.petree.global.JsendStatus;
import com.example.petree.global.ResponseSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.example.petree.global.error
 * fileName       : ErrorResponse
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 에러 응답 메시지 정형화
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@Getter
@Builder
public class ErrorResponse{

    public static Map<String, String> createErrorMessage(BindingResult bindingResult) {
        HashMap<String, String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errorMap;
    }
}
