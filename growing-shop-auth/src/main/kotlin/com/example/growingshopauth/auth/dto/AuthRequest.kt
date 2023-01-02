package com.example.growingshopauth.auth.dto

import com.example.domain.company.Company
import com.example.domain.user.User
import com.example.domain.user.UserGrade
import com.example.domain.user.UserStatus
import com.example.domain.user.UserType
import jakarta.persistence.Column
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class AuthRequest {

    class LoginReq(
        @Pattern(regexp = "^[a-zA-Z가-힣-_]+$")
        @Column(nullable = false)
        val loginId: String,

        @Column(nullable = false)
        val password: String
    )

    class JoinReq(
        @NotBlank
        @Size(max = 30)
        @Pattern(regexp = "^[a-zA-Z가-힣'-.']+$")
        val name: String,

        @Size(max = 13, min = 9)
        @NotBlank
        @Pattern(regexp = "^[0-9]{2,3}-?[0-9]{3,4}-?[0-9]{4}$")
        val mobile: String,

        @Email
        @NotBlank
        val email: String,

        @Pattern(regexp = "^[a-zA-Z가-힣'-_]+$")
        @Column(nullable = false)
        val loginId: String,

        @Column(nullable = false)
        val password: String,

        val company: Long? = null
    ) {
        fun toEntity(hashedPassword: String, company: Company?): User {
            return User(
                name = this.name,
                mobile = this.mobile,
                email = this.email,
                loginId = this.loginId,
                password = hashedPassword,
                company = company,
                status = if (company != null) UserStatus.UNDER_REVIEW else UserStatus.NORMAL,
                type = if (company != null) UserType.SELLER else UserType.NORMAL,
                grade = UserGrade.NORMAL
            )
        }
    }
}
