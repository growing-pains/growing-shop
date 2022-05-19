package com.example.growingshop.global.config.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private static String JWT_SECRET;
    private static int JWT_EXPIRATION;

    private JwtTokenProvider() {}

    @Value("${jwt.secret-key}")
    public void setJwtSecret(String jwtSecret) {
        JWT_SECRET = jwtSecret;
    }

    @Value("${jwt.expiration}")
    private void setJwtExpiration(int value) {
        JWT_EXPIRATION = value;
    }

    public static String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + JWT_EXPIRATION * 1000L);

        return Jwts.builder()
                .setSubject((String) authentication.getPrincipal())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
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
