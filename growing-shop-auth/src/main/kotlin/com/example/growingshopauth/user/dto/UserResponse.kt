package com.example.growingshopauth.user.dto

import com.example.growingshopauth.company.dto.CompanyResponse
import com.example.growingshopauth.user.domain.User
import com.example.growingshopauth.user.domain.UserGrade
import com.example.growingshopauth.user.domain.UserType

class UserResponse {

    class UserRes(
        val id: Long,
        val name: String,
        val mobile: String,
        val loginId: String,
        val email: String,
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
                    user.company?.let { CompanyResponse.CompanyRes.from(it) },
                    user.type,
                    user.grade
                )
            }
        }
    }
}
