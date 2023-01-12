package com.example.growingshopauth.auth.service

import com.example.domain.auth.Policies
import com.example.domain.auth.Policy
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.annotation.AccessibleUserTypes
import com.example.growingshopauth.auth.dto.RoleRequest
import com.example.repository.auth.PolicyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PolicyService(
    private val policyRepository: PolicyRepository
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
}
