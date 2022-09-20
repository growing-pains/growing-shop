package com.example.growingshop.global.error.config;

import com.example.growingshop.global.error.exception.InvalidJwtTokenException;
import com.example.growingshop.global.error.exception.NotAllowPathException;
import com.example.growingshop.global.error.exception.NotFoundUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
