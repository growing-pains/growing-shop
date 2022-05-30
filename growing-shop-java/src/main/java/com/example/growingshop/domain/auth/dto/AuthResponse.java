package com.example.growingshop.domain.auth.dto;

import lombok.*;

import java.time.LocalDateTime;

public class AuthResponse {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class TokenRes {
        private String token;
        private LocalDateTime expiredAt;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class JoinRes {
        private boolean state;
    }
}
