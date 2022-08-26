package com.example.growingshop.acceptance.restDocs.value;

public interface Value<T> {
    T getSuccessRequestValue();
    T getFailRequestValue();
}
