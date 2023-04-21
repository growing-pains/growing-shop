package com.example.growingshopauth.config.error

import com.example.config.error.ErrorResponse
import com.example.growingshopauth.config.error.exception.InvalidJwtTokenException
import com.example.growingshopauth.config.error.exception.NotAllowPathException
import com.example.growingshopauth.config.error.exception.NotFoundUserException
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun illegalArgumentException(exception: IllegalArgumentException): com.example.config.error.ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun entityNotFoundException(exception: EntityNotFoundException): com.example.config.error.ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(NotFoundUserException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun notFoundUserException(exception: NotFoundUserException): com.example.config.error.ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(InvalidJwtTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun invalidJwtTokenException(exception: InvalidJwtTokenException): com.example.config.error.ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(NotAllowPathException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun notAllowPathException(exception: NotAllowPathException): com.example.config.error.ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(IllegalAccessException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun illegalAccessException(exception: IllegalAccessException): com.example.config.error.ErrorResponse {
        return ErrorResponse(exception.message)
    }
}
