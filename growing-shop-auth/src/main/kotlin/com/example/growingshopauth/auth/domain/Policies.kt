package com.example.growingshopauth.auth.domain

import com.example.growingshopauth.auth.dto.RoleResponse
import jakarta.persistence.*

@Embeddable
class Policies(
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_policy",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "policy_id", referencedColumnName = "id")]
    )
    val value: List<Policy> = listOf(),
) {
    fun containPath(path: String): Boolean {
        return allAccessiblePath().any { it == path }
    }

    fun toResponse(): List<RoleResponse.PoliciesRes> {
        return value.map(RoleResponse.PoliciesRes::from)
    }

    private fun allAccessiblePath(): List<String> {
        return value.map(Policy::path)
    }
}
