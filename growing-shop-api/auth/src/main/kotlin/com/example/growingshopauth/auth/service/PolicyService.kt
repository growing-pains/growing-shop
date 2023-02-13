package com.example.growingshopauth.auth.service

import com.example.domain.auth.HttpMethod
import com.example.domain.auth.Policies
import com.example.domain.auth.Policy
import com.example.domain.user.User
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.annotation.AccessibleUserTypes
import com.example.growingshopauth.auth.dto.RoleRequest
import com.example.growingshopauth.auth.repository.ExpandRoleRepository
import com.example.repository.auth.PolicyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PolicyService(
    private val policyRepository: PolicyRepository,
    private val roleRepository: ExpandRoleRepository,
) {

    @AccessibleUserTypes(UserType.ADMIN)
    fun findAll(): Policies = Policies(policyRepository.findAll())

    @Transactional
    @AccessibleUserTypes(UserType.ADMIN)
    fun create(req: RoleRequest.CreatePolicy): Policy = policyRepository.save(req.toEntity())

    @Transactional
    @AccessibleUserTypes(UserType.ADMIN)
    fun delete(id: Long) {
        policyRepository.deleteById(id)
    }

    fun checkAccessiblePath(user: User, uri: String, method: HttpMethod) {
        val policies = Policies(policyRepository.findAll())
        val accessible = user.roles.addRoles(
            roleRepository.getByName(user.type.name).get()
        ).getAllPolicies()
            .groupBy { it.path }
            .map { (path, policies) ->
                path to policies.flatMap { it.method.getMethod() }.toSet()
            }.toMap()[uri]?.contains(method) == true

        if (policies.containPath(uri) && !accessible) {
            throw IllegalAccessException("[$method] $uri 경로에 접근 할 수 없습니다.")
        }
    }
}
