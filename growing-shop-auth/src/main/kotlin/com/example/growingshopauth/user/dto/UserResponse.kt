package com.example.growingshopauth.user.dto

import com.example.growingshopauth.user.domain.User
import com.example.growingshopauth.user.domain.UserGrade

class UserResponse {

    class UserRes(
        val id: Long,
        val name: String,
        val mobile: String,
        val loginId: String,
        val company: CompanyResponse.CompanyRes?,
        val type: UserType,
        val grade: UserGrade
    ) {
        companion object {
            fun from(user: User): UserRes {
                return UserRes(
                    user.id,
                    user.name,
                    user.mobile,
                    user.email,
                    user.loginId,
                    user.company?.let { CompanyResponse.CompanyRes.from(user.company) },
                    user.type,
                    user.grade
                )
            }
        }
    }
}
