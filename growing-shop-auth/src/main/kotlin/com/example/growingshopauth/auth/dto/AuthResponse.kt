package com.example.growingshopauth.auth.dto

import java.time.LocalDateTime

class AuthResponse {

    class TokenRes(
        val token: String,
        val expiredAt: LocalDateTime
    )
}
