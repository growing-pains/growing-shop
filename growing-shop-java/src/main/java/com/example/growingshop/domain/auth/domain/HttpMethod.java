package com.example.growingshop.domain.auth.domain;

import java.util.Set;

public enum HttpMethod {
    ALL, GET, POST, PUT, PATCH, DELETE;

    public Set<HttpMethod> getMethod() {
        if (this == HttpMethod.ALL) {
            return Set.of(HttpMethod.values());
        }

        return Set.of(this);
    }
}
