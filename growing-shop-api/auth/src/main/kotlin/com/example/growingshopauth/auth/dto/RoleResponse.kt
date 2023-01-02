package com.example.growingshopauth.auth.dto

import com.example.domain.auth.Policy
import com.example.domain.auth.Role
import com.example.growingshopauth.auth.domain.toResponse

class RoleResponse {

    class RoleRes(
        val id: Long,
        val name: String,
        val policies: List<PoliciesRes> = listOf()
    ) {
        companion object {
            fun from(role: Role): RoleRes {
                return RoleRes(
                    role.id!!,
                    role.name,
                    role.policies.toResponse()
                )
            }
        }
    }

    class PoliciesRes(
        val id: Long,
        val name: String,
        val path: String,
        val description: String
    ) {
        companion object {
            fun from(policy: Policy): PoliciesRes {
                return PoliciesRes(
                    policy.id!!,
                    policy.name,
                    policy.path,
                    policy.description
                )
            }
        }
    }
}
