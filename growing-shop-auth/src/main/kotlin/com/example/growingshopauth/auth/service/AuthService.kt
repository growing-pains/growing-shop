package com.example.growingshopauth.auth.service

import com.example.growingshopauth.auth.dto.AuthRequest
import com.example.growingshopauth.user.domain.User
import com.example.growingshopauth.user.repository.UserRepository
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {

    fun matchLoginUser(login: AuthRequest.LoginReq): Boolean =
        userRepository.findUsersByLoginId(login.loginId)
            .filter { passwordEncoder.matches(login.password, it.password) }
            .isPresent

    suspend fun loginUser(): User {
        val authentication = ReactiveSecurityContextHolder.getContext().awaitSingle()
            ?: throw IllegalAccessException("로그인된 유저 정보가 없습니다")

        return authentication.authentication.principal as User
    }
}
