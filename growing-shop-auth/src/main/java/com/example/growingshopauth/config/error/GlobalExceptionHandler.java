package com.example.growingshopauth.config.error;

import com.example.growingshopauth.config.error.exception.InvalidJwtTokenException;
import com.example.growingshopauth.config.error.exception.NotAllowPathException;
import com.example.growingshopauth.config.error.exception.NotFoundUserException;
import com.example.growingshopcommon.config.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse notFoundUserException(NotFoundUserException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse invalidJwtTokenException(InvalidJwtTokenException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(NotAllowPathException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse notAllowPathException(NotAllowPathException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
