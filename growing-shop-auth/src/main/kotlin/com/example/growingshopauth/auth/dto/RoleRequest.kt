package com.example.growingshopauth.auth.dto

import com.example.growingshopauth.auth.domain.HttpMethod
import com.example.growingshopauth.auth.domain.Policies
import com.example.growingshopauth.auth.domain.Policy
import com.example.growingshopauth.auth.domain.Role

class RoleRequest {

    class CreateRole(
        val name: String,
        val policies: List<Long> = listOf()
    ) {

        fun toEntity(policies: Policies): Role {
            return Role(
                name, policies
            )
        }
    }

    class ChangeRolePolicies(
        val role: Long,
        val policies: List<Long> = listOf()
    )

    class CreatePolicy(
        val name: String,
        val path: String,
        val method: HttpMethod,
        val description: String
    ) {
        fun toEntity(): Policy {
            return Policy(
                name, path, method, description
            )
        }
    }
}
