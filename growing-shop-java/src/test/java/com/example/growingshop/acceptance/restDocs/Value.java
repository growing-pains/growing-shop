package com.example.growingshop.acceptance.restDocs;

public interface Value<T> {
    T getSuccessRequestValue();
    T getFailRequestValue();
}
