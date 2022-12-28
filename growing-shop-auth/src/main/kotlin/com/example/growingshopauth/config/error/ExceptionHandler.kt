package com.example.growingshopauth.config.error

import com.example.growingshopauth.config.error.exception.InvalidJwtTokenException
import com.example.growingshopauth.config.error.exception.NotAllowPathException
import com.example.growingshopauth.config.error.exception.NotFoundUserException
import com.example.growingshopcommon.config.error.ErrorResponse
import com.example.growingshopcommon.config.error.GlobalExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler : GlobalExceptionHandler() {

    @ExceptionHandler(NotFoundUserException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun notFoundUserException(exception: NotFoundUserException): ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(InvalidJwtTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun invalidJwtTokenException(exception: InvalidJwtTokenException): ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(NotAllowPathException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun notAllowPathException(exception: NotAllowPathException): ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(IllegalAccessException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun illegalAccessException(exception: IllegalAccessException): ErrorResponse {
        return ErrorResponse(exception.message)
    }
}
