package org.example.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : org.example.simpledms.controller.auth
 * fileName : AuthController
 * author : GGG
 * date : 2024-04-15
 * description : 유효성 체크 예외처리
 *    유효성에 관한 에러가 발생하면 notValid() 함수가 실행된다.
 *    MethodArgumentNotValidException : 유효성 예외
 */
@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> notValid(MethodArgumentNotValidException methodArgumentNotValidException
          , HttpServletRequest request) {
    List<String> errors = new ArrayList<>();

    methodArgumentNotValidException.getAllErrors()
            .forEach(err -> errors.add(err.getDefaultMessage()));
    log.debug(errors.toString());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<String> handleException(Exception ex) {

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생: " + ex.getMessage());
  }

}
