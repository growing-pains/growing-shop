package com.example.growingshopauth.config.security;

import com.example.growingshopauth.auth.dto.AuthRequest;
import com.example.growingshopauth.auth.dto.AuthResponse;
import com.example.growingshopauth.auth.service.AuthService;
import com.example.growingshopauth.config.error.exception.InvalidJwtTokenException;
import com.example.growingshopauth.config.error.exception.NotFoundUserException;
import com.example.growingshopcommon.util.TimeUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static String JWT_SECRET;
    private static int JWT_EXPIRATION;

    private final AuthService authService;

    @Value("${jwt.secret-key}")
    public void setJwtSecret(String jwtSecret) {
        JWT_SECRET = jwtSecret;
    }

    @Value("${jwt.expiration}")
    private void setJwtExpiration(int value) {
        JWT_EXPIRATION = value;
    }

    public AuthResponse.TokenRes generateToken(AuthRequest.LoginReq login) {
        if (authService.matchLoginUser(login)) {
            Date now = new Date();
            Date expiredTime = new Date(now.getTime() + JWT_EXPIRATION * 1000L);
            String token = Jwts.builder()
                    .setSubject(login.getLoginId())
                    .setIssuedAt(now)
                    .setExpiration(expiredTime)
                    .setHeaderParam("typ", Header.JWT_TYPE)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();

            return AuthResponse.TokenRes.builder()
                    .token(token)
                    .expiredAt(TimeUtil.convertDateToLocalDateTime(expiredTime))
                    .build();
        }

        throw new NotFoundUserException("유효하지 않은 계정 정보입니다.");
    }

    public static String getUserIdFromJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.", e);
            throw new InvalidJwtTokenException("JWT 서명이 유효하지 않습니다.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.", e);
            throw new InvalidJwtTokenException("JWT 토큰이 유효하지 않습니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.", e);
            throw new InvalidJwtTokenException("JWT 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.", e);
            throw new InvalidJwtTokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            throw new InvalidJwtTokenException("JWT claim 값이 없습니다.");
        }
    }
}
