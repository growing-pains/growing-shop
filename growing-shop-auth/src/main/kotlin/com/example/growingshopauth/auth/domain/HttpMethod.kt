package com.example.growingshopauth.auth.domain

enum class HttpMethod {
    ALL, GET, POST, PUT, PATCH, DELETE;

    fun getMethod(): Set<HttpMethod> {
        if (this == ALL) return HttpMethod.values().toSet()
        return setOf(this)
    }
}