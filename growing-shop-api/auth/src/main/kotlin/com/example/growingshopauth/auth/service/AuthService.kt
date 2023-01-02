package com.example.growingshopauth.auth.service

import com.example.growingshopauth.auth.dto.AuthRequest
import com.example.growingshopauth.user.repository.ExpandUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: ExpandUserRepository
) {

    fun matchLoginUser(login: AuthRequest.LoginReq): Boolean =
        userRepository.findUsersByLoginId(login.loginId)
            .filter { passwordEncoder.matches(login.password, it.password) }
            .isPresent
}
