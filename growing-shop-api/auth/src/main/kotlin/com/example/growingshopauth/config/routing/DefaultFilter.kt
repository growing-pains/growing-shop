package com.example.growingshopauth.config.routing

import com.example.domain.auth.HttpMethod
import com.example.domain.auth.Role
import com.example.domain.user.User
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.service.PolicyService
import com.example.growingshopauth.auth.service.RoleService
import com.example.growingshopauth.config.security.JwtTokenProvider
import com.example.growingshopauth.user.service.UserService
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
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
) : GlobalFilter {

    private val userSession: RMapCache<String, User> = redissonClient.getMapCache(redisKey)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return chain.filter(
            setUserSessionInRedis(exchange)
        )
    }

    fun setUserSessionInRedis(exchange: ServerWebExchange): ServerWebExchange {
        val req = exchange.request
        val token = req.headers
            .getFirst(HttpHeaders.AUTHORIZATION)
            ?.substring(AUTH_HEADER_PREFIX.length)
            ?: throw IllegalAccessException("인증 정보를 찾을 수 없습니다.")
        val uuid = JwtTokenProvider.getUUIDFromJwt(token)
        val uri = req.uri.path
        val method = HttpMethod.valueOf(req.method.name())
        val user = getUserByAuthorization(token)

        checkAccessiblePath(user, uri, method)

        userSession.put(
            uuid,
            user,
            JwtTokenProvider.getJwtRemainExpirationMillis(token),
            TimeUnit.MILLISECONDS
        )

        return exchange.mutate().request(
            req.mutate()
                .header(HttpHeaders.AUTHORIZATION, uuid)
                .build()
        ).build()
    }

    private fun getUserByAuthorization(token: String): User {
        val userId = JwtTokenProvider.getUserIdFromJwt(token)

        return userService.getUserByLoginId(userId)
    }

    private fun checkAccessiblePath(user: User, uri: String, method: HttpMethod) {
        val policies = policyService.findAll()
        val accessible = user.roles.addRoles(getUserTypeRole(user.type))
            .getAllPolicies()
            .groupBy { it.path }
            .map { (path, policies) ->
                path to policies.flatMap { it.method.getMethod() }.toSet()
            }.toMap()[uri]?.contains(method) == true

        if (policies.containPath(uri) && !accessible) {
            throw IllegalAccessException("[$method] $uri 경로에 접근 할 수 없습니다.")
        }
    }

    private fun getUserTypeRole(userType: UserType): Role {
        return roleService.findByName(userType.name)
    }

    companion object {
        private const val AUTH_HEADER_PREFIX = "Bearer "
    }
}
