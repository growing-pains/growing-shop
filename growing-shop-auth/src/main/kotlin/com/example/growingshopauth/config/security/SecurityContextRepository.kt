package com.example.growingshopauth.config.security

import com.example.growingshopauth.auth.domain.HttpMethod
import com.example.growingshopauth.auth.domain.Role
import com.example.growingshopauth.auth.service.PolicyService
import com.example.growingshopauth.auth.service.RoleService
import com.example.growingshopauth.config.error.exception.NotAllowPathException
import com.example.growingshopauth.user.domain.User
import com.example.growingshopauth.user.domain.UserType
import com.example.growingshopauth.user.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KLogging
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
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
        TODO("Not yet implemented")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        return Mono.justOrEmpty(exchange.request.headers.getFirst(AUTH_HEADER))
            .filter { it.startsWith(AUTH_HEADER_PREFIX) }
            .flatMap {
                try {
                    val context = SecurityContextImpl(
                        runBlocking { setAuthenticate(it) }
                    )
                    checkAccessiblePath(
                        exchange.request.uri.path,
                        HttpMethod.valueOf(exchange.request.method.name())
                    )

                    context.toMono()
                } catch (e: Exception) {
                    logger.warn("유저 인증도중 에러가 발생하였습니다.", e)
                    Mono.empty()
                }
            }
    }

    private suspend fun setAuthenticate(authorization: String): Authentication {
        val user = getUserByAuthorization(authorization)
        val authority = Authority(
            user.loginId,
            getUserTypeRole(user.type),
            user.roles
        )

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

    private fun checkAccessiblePath(uri: String, method: HttpMethod) {
        if (isNotAccessiblePath(uri, method)) {
            throw IllegalAccessException("요청한 경로에 접근 할 수 없습니다.")
        }
    }

    private fun isNotAccessiblePath(path: String, method: HttpMethod): Boolean {
        return if (policyService.findAll().containPath(path)) {
            return !getUserAuthority().possibleAccess(path, method)
        } else false
    }

    private fun getUserAuthority(): Authority {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw NotAllowPathException("요청에 대한 인증 정보가 없습니다.")

        if (authentication !is UserAuthentication) throw NotAllowPathException("로그인 된 유저 정보의 데이터가 잘못되었습니다")

        val authority = authentication.authorities.first()

        if (authority !is Authority) throw NotAllowPathException("로그인 된 유저의 인증 정보가 잘못되었습니다.")

        return authority
    }

    companion object: KLogging() {
        private const val AUTH_HEADER = "Authorization"
        private const val AUTH_HEADER_PREFIX = "Bearer "
    }
}
