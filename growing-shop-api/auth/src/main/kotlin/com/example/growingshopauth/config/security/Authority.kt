package com.example.growingshopauth.config.security

import com.example.domain.auth.HttpMethod
import com.example.domain.auth.Role
import com.example.domain.auth.Roles
import org.springframework.security.core.GrantedAuthority

class Authority(
    private val name: String,
    private val userTypeRole: Role,
    private val userRoles: Roles
) : GrantedAuthority {
    private val accessible: Map<String, Set<HttpMethod>> = userRoles.addRoles(userTypeRole).getAllPolicies()
        .groupBy { it.path }
        .map { (path, policies) ->
            path to policies.map { it.method }.toSet()
        }.toMap()

    override fun getAuthority(): String {
        return ROLE_PREFIX + name
    }

    fun possibleAccess(path: String, httpMethod: HttpMethod): Boolean {
        return accessible[path]?.contains(httpMethod) ?: false
    }

    companion object {
        private const val ROLE_PREFIX = "ROLE_"
    }
}
