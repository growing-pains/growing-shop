package com.example.growingshopauth.config.security

import com.example.growingshopauth.auth.dto.AuthRequest
import com.example.growingshopauth.auth.dto.AuthResponse
import com.example.growingshopauth.auth.service.AuthService
import com.example.growingshopauth.config.error.exception.InvalidJwtTokenException
import com.example.growingshopauth.config.error.exception.NotFoundUserException
import com.example.growingshopcommon.util.TimeUtil
import io.jsonwebtoken.*
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    private val jwtSecret: String,
    @Value("\${jwt.expiration}")
    private val jwtExpiration: String,
    private val authService: AuthService
) {

    init {
        JWT_SECRET = jwtSecret
        JWT_EXPIRATION = jwtExpiration
    }

    fun generateToken(login: AuthRequest.LoginReq): AuthResponse.TokenRes {
        if (authService.matchLoginUser(login)) {
            val now = Date()
            val expiredTime = Date(now.time + JWT_EXPIRATION.toLong() * 1_000)
            val token = Jwts.builder()
                .setSubject(login.loginId)
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact()

            return AuthResponse.TokenRes(
                token,
                TimeUtil.convertDateToLocalDateTime(expiredTime)
            )
        }

        throw NotFoundUserException("유효하지 않은 계정 정보입니다.");
    }

    companion object : KLogging() {
        lateinit var JWT_SECRET: String
        lateinit var JWT_EXPIRATION: String

        fun getUserIdFromJwt(token: String): String {
            return try {
                Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .body
                    .subject
            } catch (e: SignatureException) {
                logger.error("Invalid JWT signature.", e)
                throw InvalidJwtTokenException("JWT 서명이 유효하지 않습니다.")
            } catch (e: MalformedJwtException) {
                logger.error("Invalid JWT token.", e);
                throw InvalidJwtTokenException("JWT 토큰이 유효하지 않습니다.");
            } catch (e: ExpiredJwtException) {
                logger.error("Expired JWT token.", e);
                throw InvalidJwtTokenException("JWT 토큰이 만료되었습니다.");
            } catch (e: UnsupportedJwtException) {
                logger.error("Unsupported JWT token.", e);
                throw InvalidJwtTokenException("지원되지 않는 JWT 토큰입니다.");
            } catch (e: IllegalArgumentException) {
                logger.error("JWT claims string is empty.", e);
                throw InvalidJwtTokenException("JWT claim 값이 없습니다.");
            }
        }
    }
}
