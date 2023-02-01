package com.example.growingshopauth.auth.service

import com.example.domain.auth.Policies
import com.example.domain.auth.Role
import com.example.domain.auth.Roles
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.annotation.AccessibleUserTypes
import com.example.growingshopauth.auth.domain.changePolicies
import com.example.growingshopauth.auth.dto.RoleRequest
import com.example.growingshopauth.auth.repository.ExpandRoleRepository
import com.example.repository.auth.PolicyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RoleService(
    private val roleRepository: ExpandRoleRepository,
    private val policyRepository: PolicyRepository
) {

    @AccessibleUserTypes(UserType.ADMIN)
    fun findAll(): Roles = Roles(roleRepository.findAll())

    @AccessibleUserTypes(UserType.ADMIN)
    fun getById(id: Long): Role = roleRepository.findById(id)
        .orElseThrow { IllegalArgumentException("$id 에 해당하는 role 을 찾을 수 없습니다.") }

    @AccessibleUserTypes(UserType.ADMIN)
    fun findByName(name: String): Role = roleRepository.getByName(name)
        .orElseThrow { IllegalArgumentException("$name 에 해당하는 role 을 찾을 수 없습니다.") }

    @Transactional
    @AccessibleUserTypes(UserType.ADMIN)
    fun create(req: RoleRequest.CreateRole): Role = roleRepository.save(
        req.toEntity(
            Policies(policyRepository.findAllById(req.policies))
        )
    )

    @Transactional
    @AccessibleUserTypes(UserType.ADMIN)
    fun changePolicies(req: RoleRequest.ChangeRolePolicies) {
        val role = getById(req.role)
        val policies = Policies(policyRepository.findAllById(req.policies))

        role.changePolicies(policies)
    }

    @Transactional
    @AccessibleUserTypes(UserType.ADMIN)
    fun delete(id: Long) {
        roleRepository.deleteById(id)
    }
}
