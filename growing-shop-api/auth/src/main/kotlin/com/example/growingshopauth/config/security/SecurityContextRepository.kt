//package com.example.growingshopauth.config.security
//
//import com.example.domain.user.User
//import mu.KLogging
//import org.redisson.api.RMapCache
//import org.redisson.api.RedissonClient
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.http.HttpHeaders
//import org.springframework.http.server.reactive.ServerHttpRequest
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.context.SecurityContext
//import org.springframework.security.core.context.SecurityContextImpl
//import org.springframework.security.web.server.context.ServerSecurityContextRepository
//import org.springframework.stereotype.Component
//import org.springframework.web.server.ServerWebExchange
//import reactor.core.publisher.Mono
//import reactor.kotlin.core.publisher.toMono
//import java.util.*
//
//@Component
//class SecurityContextRepository(
//    private val redissonClient: RedissonClient,
//    @Value("\${redis.auth-key}")
//    private val redisKey: String
//) : ServerSecurityContextRepository {
//
//    private val userSession: RMapCache<String, User> = redissonClient.getMapCache(redisKey)
//
//    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
//        return Mono.empty()
//    }
//
//    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
//        return Mono.justOrEmpty(exchange.request)
//            .flatMap { req ->
//                try {
////                    SecurityContextImpl(
////                        setAuthenticate(req)
////                    ).toMono()
//                    SecurityContextImpl().toMono()
//                } catch (e: Exception) {
//                    logger.warn("유저 인증도중 에러가 발생하였습니다.", e)
//                    Mono.error { throw IllegalAccessException(e.message) }
//                }
//            }
//    }
//
//    private fun setAuthenticate(req: ServerHttpRequest): Authentication {
//        val uuid = req.headers
//            .getFirst(HttpHeaders.AUTHORIZATION)
//            ?: throw throw RuntimeException("유저 정보를 찾기위한 key 가 없습니다.")
//
//        if (!uuid.matches(UUID_REGEX)) throw RuntimeException("유효하지 않은 key 입니다.")
//
//        val user = userSession[uuid] ?: throw RuntimeException("유저를 찾을 수 없습니다.")
//
//        return UsernamePasswordAuthenticationToken(user, null)
//    }
//
//    companion object : KLogging() {
//        private val UUID_REGEX = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
//    }
//}

package com.example.growingshopauth.config.security

import com.example.domain.user.User
import com.example.growingshopauth.auth.service.PolicyService
import com.example.growingshopauth.auth.service.RoleService
import com.example.growingshopauth.user.service.UserService
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@Component
class SecurityContextRepository(
    private val userService: UserService,
    private val policyService: PolicyService,
    private val roleService: RoleService,
    private val redissonClient: RedissonClient,
    @Value("\${redis.auth-key}")
    private val redisKey: String
) : ServerSecurityContextRepository {

    private val userSession: RMapCache<String, User> = redissonClient.getMapCache(redisKey)

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        return Mono.justOrEmpty(exchange.request)
            .filter { req ->
                req.headers.getFirst(HttpHeaders.AUTHORIZATION)?.startsWith(AUTH_HEADER_PREFIX)
                    ?: throw IllegalAccessException("인증 정보가 없습니다.")
            }
            .flatMap { req ->
                try {
                    SecurityContextImpl(
                        runBlocking {
                            setAuthenticate(req)
                        }
                    ).toMono()
                } catch (e: Exception) {
                    logger.warn("유저 인증도중 에러가 발생하였습니다.", e)
                    Mono.error { throw IllegalAccessException(e.message) }
                }
            }
    }

    private suspend fun setAuthenticate(req: ServerHttpRequest): Authentication {
        val uuid = req.headers
            .getFirst(HttpHeaders.AUTHORIZATION)!!
            .substring(AUTH_HEADER_PREFIX.length)
        val user = userSession[uuid]

        return UsernamePasswordAuthenticationToken(user, null)
    }

    companion object : KLogging() {
        private const val AUTH_HEADER_PREFIX = "Bearer "
    }
}

