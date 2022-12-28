package com.example.growingshopcommon.config.error;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Deprecated
@RestControllerAdvice
public class GlobalExceptionHandler {
    // TODO - 모든 외부 라이브러리를 common 모듈에서 제거하기 위해, 해당 전역 설정은 api 모듈 전역 설정으로 변경 후 common 에 존재하는 외부 라이브러리는 모두 지운다

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentException(IllegalArgumentException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse entityNotFoundException(EntityNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
