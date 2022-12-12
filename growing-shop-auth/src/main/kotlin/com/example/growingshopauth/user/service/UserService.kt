package com.example.growingshopauth.user.service

import com.example.growingshopauth.auth.dto.AuthRequest
import com.example.growingshopauth.company.repository.CompanyRepository
import com.example.growingshopauth.user.domain.User
import com.example.growingshopauth.user.dto.UserResponse
import com.example.growingshopauth.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
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

    fun getLoginUser(): User {
        TODO("security 컨버팅 후 context 에서 뽑아오는 로직 추가")
    }
}
