package com.example.petree.global.error;

import com.example.petree.global.Response;
import com.example.petree.global.error.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

/**
 * packageName    : com.example.petree.global.error
 * fileName       : GlobalExceptionHandler
 * author         : 박수현
 * date           : 2023-07-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Response response;

    /**
     * @author 박수현
     * @date 2023-08-24
     * @description : 기존비밀번호, 새롭게 등록한 비밀번호가 다른경우 에러 처리
     * @return
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleDuplicateException(IncorrectPasswordException e) {
        return response.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 중복 에러 처리
     * @return
     */
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> handleDuplicateException(DuplicateException e) {
        return response.fail(HttpStatus.OK, e.getMessage());
    }
    /**
     * javax.validation.Valid 또는 @Validated binding error가 발생할 경우
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<?> handleBindException(BindException e) {
        log.error("handleBindException", e);
        return response.fail(HttpStatus.BAD_REQUEST, Map.of("message", e.getMessage()));
    }

    /**
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        return response.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return response.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 비즈니스 로직 실행 중 오류 발생
     */
    @ExceptionHandler(value = { BusinessException.class })
    protected ResponseEntity<?> handleConflict(BusinessException e) {
        log.error("BusinessException", e);
        return response.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MissingPrincipalException.class)
    protected ResponseEntity<?> handleMissingPrincipalException(MissingPrincipalException e) {
        log.error("MissingPrincipalException", e);
        return response.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(value = { ServerException.class })
    protected ResponseEntity<?> ServerRelatedException(ServerException e) {
        log.error("ServerRelatedException", e);
        return response.error(e.getMessage());
    }


    /**
     * 나머지 예외 발생
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        log.error("Exception", e);
        return response.error(e.getMessage());
    }
}
