package com.example.growingshopauth.config.routing

import com.example.domain.user.User
import com.example.growingshopauth.config.error.exception.NotFoundUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext


class UserContext {
    companion object {

        fun loginUser(): User {
            val principal = runBlocking(Dispatchers.IO) {
                ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(Authentication::getPrincipal)
                    .awaitSingleOrNull() ?: throw NotFoundUserException("유저 정보가 없습니다.")
            }

            if (principal !is User) {
                throw NotFoundUserException("로그인 된 유저 정보의 데이터가 잘못되었습니다.")
            }

            return principal
        }
    }
}
