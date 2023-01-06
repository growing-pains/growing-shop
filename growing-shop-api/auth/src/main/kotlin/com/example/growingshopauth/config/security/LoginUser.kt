package com.example.growingshopauth.config.security

import com.example.domain.user.User
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder

class LoginUser {
    companion object {
        suspend fun getUserInSecurityContext(): User {
            val authentication = ReactiveSecurityContextHolder.getContext().awaitSingle()
                ?: throw IllegalAccessException("로그인된 유저 정보가 없습니다")

            return authentication.authentication.principal as User
        }
    }
}
