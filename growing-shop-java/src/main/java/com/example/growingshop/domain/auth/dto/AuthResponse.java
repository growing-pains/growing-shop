package com.example.growingshop.domain.auth.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class TokenRes {
        private String token;
        private LocalDateTime expiredAt;
    }
}
