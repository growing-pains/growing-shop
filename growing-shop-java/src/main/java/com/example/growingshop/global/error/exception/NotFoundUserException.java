package com.example.growingshop.global.error.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
