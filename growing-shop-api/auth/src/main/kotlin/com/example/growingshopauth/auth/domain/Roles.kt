package com.example.growingshopauth.auth.domain

import com.example.domain.auth.Roles
import com.example.growingshopauth.auth.dto.RoleResponse

fun Roles.toResponse(): List<RoleResponse.RoleRes> {
    return value.map(RoleResponse.RoleRes::from)
}
