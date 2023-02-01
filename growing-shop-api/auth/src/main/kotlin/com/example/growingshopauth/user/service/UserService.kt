package com.example.growingshopauth.user.service

import com.example.domain.user.User
import com.example.growingshopauth.auth.dto.AuthRequest
import com.example.growingshopauth.config.error.exception.NotFoundUserException
import com.example.growingshopauth.user.dto.UserResponse
import com.example.growingshopauth.user.repository.ExpandUserRepository
import com.example.repository.company.CompanyRepository
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: ExpandUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val companyRepository: CompanyRepository
) {

    @Transactional
    fun joinUser(join: AuthRequest.JoinReq): UserResponse.UserRes {
        if (userRepository.findUsersByLoginId(join.loginId).isPresent) {
            throw IllegalArgumentException("이미 가입된 아이디입니다.");
        }

        return UserResponse.UserRes.from(
            userRepository.save(
                join.toEntity(
                    hashedPassword = passwordEncoder.encode(join.password),
                    company = join.company?.let {
                        companyRepository.findById(it)
                            .orElseThrow { IllegalArgumentException("$it 에 해당하는 업체가 존재하지 않습니다.") }
                    }
                )
            )
        )
    }

    fun getUserByLoginId(loginId: String): User = userRepository.findUsersByLoginId(loginId)
        .orElseThrow { NotFoundUserException("$loginId 에 해당하는 유저 정보를 찾을 수 없습니다.") }

    suspend fun getLoginUser(): User {
        val authentication = ReactiveSecurityContextHolder.getContext().awaitSingle()
            ?: throw IllegalAccessException("로그인된 유저 정보가 없습니다")

        return authentication.authentication.principal as User
    }
}
