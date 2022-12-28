package com.example.growingshopauth.config.security

import com.example.growingshopauth.auth.domain.HttpMethod
import com.example.growingshopauth.auth.domain.Role
import com.example.growingshopauth.auth.service.PolicyService
import com.example.growingshopauth.auth.service.RoleService
import com.example.growingshopauth.user.domain.User
import com.example.growingshopauth.user.domain.UserType
import com.example.growingshopauth.user.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class SecurityContextRepository(
    private val userService: UserService,
    private val policyService: PolicyService,
    private val roleService: RoleService
): ServerSecurityContextRepository {
    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        return Mono.justOrEmpty(exchange.request)
            .filter {
                it.headers.getFirst(HttpHeaders.AUTHORIZATION)?.startsWith(AUTH_HEADER_PREFIX)
                    ?: throw IllegalAccessException("인증 정보가 없습니다.")
            }
            .flatMap {
                try {
                    SecurityContextImpl(
                        runBlocking {
                            setAuthenticate(
                                it.headers.getFirst(HttpHeaders.AUTHORIZATION)!!,
                                it.uri.path,
                                HttpMethod.valueOf(it.method.name())
                            )
                        }
                    ).toMono()
                } catch (e: Exception) {
                    logger.warn("유저 인증도중 에러가 발생하였습니다.", e)
                    Mono.error { throw IllegalAccessException(e.message) }
                }
            }
    }

    private suspend fun setAuthenticate(authorization: String, uri: String, method: HttpMethod): Authentication {
        val user = getUserByAuthorization(authorization)
        val authority = Authority(
            user.loginId,
            getUserTypeRole(user.type),
            user.roles
        )

        checkAccessiblePath(authority, uri, method)

        return UserAuthentication(user, authority)
    }

    private suspend fun getUserByAuthorization(value: String): User {
        val token = value.substring(AUTH_HEADER_PREFIX.length)
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
