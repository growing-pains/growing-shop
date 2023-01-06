package com.example.growingshopauth.config.routing

import com.example.domain.auth.HttpMethod
import com.example.domain.auth.Role
import com.example.domain.user.User
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.service.PolicyService
import com.example.growingshopauth.auth.service.RoleService
import com.example.growingshopauth.config.security.Authority
import com.example.growingshopauth.config.security.JwtTokenProvider
import com.example.growingshopauth.user.service.UserService
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Configuration
class DefaultFilter(
    @Value("\${redis.auth-key}")
    private val redisKey: String,
    private val userService: UserService,
    private val roleService: RoleService,
    private val policyService: PolicyService,
    private val redissonClient: RedissonClient
) : GlobalFilter, Ordered {

    private val userSession: RMapCache<String, User> = redissonClient.getMapCache(redisKey)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return chain.filter(
            exchange.mutate().request(
                setUserSessionInRedis(exchange.request)
            ).build()
        )
    }

    fun setUserSessionInRedis(req: ServerHttpRequest): ServerHttpRequest {
        val token = req.headers
            .getFirst(HttpHeaders.AUTHORIZATION)
            ?.substring(AUTH_HEADER_PREFIX.length)
            ?: throw IllegalAccessException("인증 정보를 찾을 수 없습니다.")
        val uuid = JwtTokenProvider.getUUIDFromJwt(token)
        val uri = req.uri.path
        val method = HttpMethod.valueOf(req.method.name())
        val user = getUserByAuthorization(token)
        val authority = Authority(
            user.loginId,
            getUserTypeRole(user.type),
            user.roles
        )

        checkAccessiblePath(authority, uri, method)

        userSession.put(
            uuid,
            user,
            JwtTokenProvider.getJwtRemainExpirationMillis(token),
            TimeUnit.MILLISECONDS
        )
        ReactiveSecurityContextHolder.withAuthentication(
            UsernamePasswordAuthenticationToken(user, null)
        )

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null)

        return req.mutate()
            .header(HttpHeaders.AUTHORIZATION, uuid)
            .build()
    }

    private fun getUserByAuthorization(token: String): User {
        val userId = JwtTokenProvider.getUserIdFromJwt(token)

        return userService.getUserByLoginId(userId)
    }

    private fun getUserTypeRole(userType: UserType): Role {
        return roleService.findByName(userType.name)
    }

    private fun checkAccessiblePath(authority: Authority, uri: String, method: HttpMethod) {
        val policies = policyService.findAll()

        if (policies.containPath(uri) && !authority.possibleAccess(uri, method)) {
            throw IllegalAccessException("[$method] $uri 경로에 접근 할 수 없습니다.")
        }
    }

    override fun getOrder(): Int {
        return Integer.MAX_VALUE
    }

    companion object {
        private const val AUTH_HEADER_PREFIX = "Bearer "
    }
}
