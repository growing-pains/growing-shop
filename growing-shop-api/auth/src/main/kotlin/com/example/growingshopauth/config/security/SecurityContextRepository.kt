package com.example.growingshopauth.config.security

import com.example.domain.auth.HttpMethod
import com.example.domain.auth.Role
import com.example.domain.user.User
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.service.PolicyService
import com.example.growingshopauth.auth.service.RoleService
import com.example.growingshopauth.user.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KLogging
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*
import java.util.concurrent.TimeUnit

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
        return Mono.justOrEmpty(exchange.request to exchange.response)
            .filter { (req, _) ->
                req.headers.getFirst(HttpHeaders.AUTHORIZATION)?.startsWith(AUTH_HEADER_PREFIX)
                    ?: throw IllegalAccessException("인증 정보가 없습니다.")
            }
            .flatMap { (req, res) ->
                try {
                    SecurityContextImpl(
                        runBlocking {
                            setAuthenticate(req, res)
                        }
                    ).toMono()
                } catch (e: Exception) {
                    logger.warn("유저 인증도중 에러가 발생하였습니다.", e)
                    Mono.error { throw IllegalAccessException(e.message) }
                }
            }
    }

    private suspend fun setAuthenticate(req: ServerHttpRequest, res: ServerHttpResponse): Authentication {
        val uuid = UUID.randomUUID().toString()
        val token = req.headers
            .getFirst(HttpHeaders.AUTHORIZATION)!!
            .substring(AUTH_HEADER_PREFIX.length)
        val uri = req.uri.path
        val method = HttpMethod.valueOf(req.method.name())
        val user = getUserByAuthorization(token)
        val authority = Authority(
            user.loginId,
            getUserTypeRole(user.type),
            user.roles
        )

        checkAccessiblePath(authority, uri, method)

        userSession.put(uuid, user, JwtTokenProvider.getJwtRemainExpirationMillis(token), TimeUnit.MILLISECONDS)
        res.headers.add(HttpHeaders.AUTHORIZATION, uuid)

        return UserAuthentication(user, authority)
    }

    private suspend fun getUserByAuthorization(token: String): User {
        val userId = JwtTokenProvider.getUserIdFromJwt(token)

        return withContext(Dispatchers.IO) {
            userService.getUserByLoginId(userId)
        }
    }

    private suspend fun getUserTypeRole(userType: UserType): Role {
        return withContext(Dispatchers.IO) {
            roleService.findByName(userType.name)
        }
    }

    private suspend fun checkAccessiblePath(authority: Authority, uri: String, method: HttpMethod) {
        val policies = withContext(Dispatchers.IO) {
            policyService.findAll()
        }

        if (policies.containPath(uri) && !authority.possibleAccess(uri, method)) {
            throw IllegalAccessException("[$method] $uri 경로에 접근 할 수 없습니다.")
        }
    }

    companion object : KLogging() {
        private const val AUTH_HEADER_PREFIX = "Bearer "
    }
}
