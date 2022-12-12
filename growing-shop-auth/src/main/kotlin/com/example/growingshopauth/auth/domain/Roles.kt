package com.example.growingshopauth.auth.domain

import com.example.growingshopauth.auth.dto.RoleResponse
import jakarta.persistence.*

@Embeddable
class Roles(
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val value: List<Role> = listOf()
) {
    fun addRoles(other: Role): Roles {
        return Roles(
            value + listOf(other)
        )
    }

    fun getAllPolicies(): List<Policy> {
        return value.flatMap { it.policies.value }
    }

    fun toResponse(): List<RoleResponse.RoleRes> {
        return value.map(RoleResponse.RoleRes::from)
    }
}