package com.example.growingshopauth.auth.domain

import com.example.domain.auth.Policies
import com.example.growingshopauth.auth.dto.RoleResponse

fun Policies.toResponse(): List<RoleResponse.PoliciesRes> {
    return value.map(RoleResponse.PoliciesRes::from)
}
