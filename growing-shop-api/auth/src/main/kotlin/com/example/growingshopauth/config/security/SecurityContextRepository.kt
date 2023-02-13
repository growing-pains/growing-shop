package com.example.growingshopauth.config.security

import com.example.domain.auth.HttpMethod
import com.example.domain.auth.Role
import com.example.domain.user.User
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.repository.ExpandRoleRepository
import com.example.growingshopauth.auth.service.PolicyService
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
    private val roleRepository: ExpandRoleRepository,
) : ServerSecurityContextRepository {
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
                    val authentication = runBlocking {
                        setAuthenticate(
                            it.headers.getFirst(HttpHeaders.AUTHORIZATION)!!,
                            it.uri.path,
                            HttpMethod.valueOf(it.method.name())
                        )
                    }

                    val test = SecurityContextImpl(
                        authentication
                    )

                    test.toMono()

//                    SecurityContextImpl(
//                        authentication
//                    ).toMono()
                } catch (e: Exception) {
                    logger.warn("유저 인증도중 에러가 발생하였습니다.", e)
                    Mono.error { throw IllegalAccessException(e.message) }
                }
            }
    }

    private suspend fun setAuthenticate(authorization: String, uri: String, method: HttpMethod): Authentication {
        val user = getUserByAuthorization(authorization)

        withContext(Dispatchers.IO) {
            policyService.checkAccessiblePath(user, uri, method)
        }

        return UserAuthentication(
            user,
            Authority(
                user.loginId,
                getUserTypeRole(user.type),
                user.roles
            )
        )
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
            roleRepository.getByName(userType.name)
                .orElseThrow { IllegalArgumentException("${userType.name} 에 해당하는 role 을 찾을 수 없습니다.") }
        }
    }

    companion object : KLogging() {
        private const val AUTH_HEADER_PREFIX = "Bearer "
    }
}
