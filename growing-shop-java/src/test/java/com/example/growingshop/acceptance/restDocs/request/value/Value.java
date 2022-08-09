package com.example.growingshop.acceptance.restDocs.request.value;

import org.springframework.stereotype.Component;

@Component
public interface Value<T> {
    T getSuccessRequestValue();
    T getFailRequestValue();
}
