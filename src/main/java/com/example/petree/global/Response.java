package com.example.petree.global;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * packageName    : com.example.petree.global
 * fileName       : Response
 * author         : 정세창
 * date           : 2023-07-02
 * description    : 응답 형식을 통일하기 위한 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-02        정세창              최초 생성
 * 2023-07-31        정세창          Jsend 형식에 맞춰 수정
 */

@Component
public class Response {

    @Getter
    @Builder
    private static class Body {

        private JsendStatus status;
        private Object data;
    }


    public ResponseEntity<?> success(HttpStatus status, Object data) {
        Body body = Body.builder()
                .status(JsendStatus.SUCCESS)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    /**
     * 생성, 수정, 삭제 등이 성공했을 때 주로 사용(data에 담을게 없을 때)
     * @param status HTTP status 코드
     * @return
     */

    public ResponseEntity<?> success(HttpStatus status) {
        return success(status, null);
    }


    public ResponseEntity<?> fail(HttpStatus status, Object data) {
        Body body = Body.builder()
                .status(JsendStatus.FAIL)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    /**
     *
     * @param data
     * @return
     */
    public ResponseEntity<?> error(Object data) {
        Body body = Body.builder()
                .status(JsendStatus.ERROR)
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}