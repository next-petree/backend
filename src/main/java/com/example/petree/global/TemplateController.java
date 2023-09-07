package com.example.petree.global;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : com.example.petree.global
 * fileName       : TemplateController
 * author         : qkrtn_ulqpbq2
 * date           : 2023-06-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-30        qkrtn_ulqpbq2       최초 생성
 */
@RequiredArgsConstructor
public class TemplateController {

    private final Response response;

    /**
     *
     * @param param1
     * @param parma2
     * @return
     */
    public ResponseEntity<?> TemplateMethod(Integer param1, String parma2) {
        return response.success(HttpStatus.OK);
    }

}
