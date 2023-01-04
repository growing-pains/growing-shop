package com.example.growingshopauth.config.filter

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
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import java.util.concurrent.TimeUnit

@Configuration
class FilterConfig(
    @Value("\${growing-shop.endpoints.audience}")
    private val audiencePath: String,
    @Value("\${growing-shop.endpoints.auth}")
    private val authPath: String,
    @Value("\${growing-shop.endpoints.order}")
    private val orderPath: String,
    @Value("\${growing-shop.endpoints.product}")
    private val productPath: String,
    @Value("\${redis.auth-key}")
    private val redisKey: String,
    private val userService: UserService,
    private val roleService: RoleService,
    private val policyService: PolicyService,
    private val redissonClient: RedissonClient
) {

    private val userSession: RMapCache<String, User> = redissonClient.getMapCache(redisKey)

    @Bean
    fun gatewayRoutes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("growing-shop-audience") {
                it.path("/audience/**").filters { filterSpec ->
                    filterSpec.filter { exchange, chain ->
                        setUserSessionInRedis(exchange.request, exchange.response)

                        chain.filter(exchange)
                    }
                }.uri(audiencePath)
            }.route("growing-shop-auth") {
                it.path("/auth/**").filters { filterSpec ->
                    filterSpec.filter { exchange, chain ->
                        setUserSessionInRedis(exchange.request, exchange.response)

                        chain.filter(exchange)
                    }
                }.uri(authPath)
            }.route("growing-shop-order") {
                it.path("/order/**").filters { filterSpec ->
                    filterSpec.filter { exchange, chain ->
                        setUserSessionInRedis(exchange.request, exchange.response)

                        chain.filter(exchange)
                    }
                }.uri(orderPath)
            }.route("growing-shop-product") {
                it.path("/product/**").filters { filterSpec ->
                    filterSpec.filter { exchange, chain ->
                        setUserSessionInRedis(exchange.request, exchange.response)

                        chain.filter(exchange)
                    }
                }.uri(productPath)
            }.build()
    }

    private fun setUserSessionInRedis(req: ServerHttpRequest, res: ServerHttpResponse) {
        val token = req.headers
            .getFirst(HttpHeaders.AUTHORIZATION)
            ?.substring(AUTH_HEADER_PREFIX.length)
            ?: return
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

        userSession.putIfAbsent(
            uuid,
            user,
            JwtTokenProvider.getJwtRemainExpirationMillis(token),
            TimeUnit.MILLISECONDS
        )
        res.headers.add(HttpHeaders.AUTHORIZATION, uuid)
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

    companion object {
        private const val AUTH_HEADER_PREFIX = "Bearer "
    }
}
