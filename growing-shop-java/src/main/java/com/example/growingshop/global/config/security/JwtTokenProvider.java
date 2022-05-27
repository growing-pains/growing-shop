package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.dto.AuthRequest;
import com.example.growingshop.domain.auth.dto.AuthResponse;
import com.example.growingshop.domain.auth.service.AuthService;
import com.example.growingshop.global.util.TimeUtil;
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

    public AuthResponse.TokenRes generateToken(AuthRequest.LoginReq login) throws IllegalAccessException {
        if (authService.matchLoginUser(login)) {
            Date now = new Date();
            Date expiredTime = new Date(now.getTime() + JWT_EXPIRATION * 1000L);
            String token = Jwts.builder()
                    .setSubject(login.getLoginId())
                    .setIssuedAt(now)
                    .setExpiration(expiredTime)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();

            return AuthResponse.TokenRes.builder()
                    .token(token)
                    .expiredAt(TimeUtil.convertDateToLocalDateTime(expiredTime))
                    .build();
        }

        throw new IllegalAccessException("Invalid account information.");
    }

    public static String getUserIdFromJwt(String token) throws IllegalAccessException {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.", e);
            throw new IllegalAccessException("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.", e);
            throw new IllegalAccessException("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.", e);
            throw new IllegalAccessException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.", e);
            throw new IllegalAccessException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            throw new IllegalAccessException("JWT claims string is empty..");
        }
    }
}
