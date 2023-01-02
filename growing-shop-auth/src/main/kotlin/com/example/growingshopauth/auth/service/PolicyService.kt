package com.example.growingshopauth.auth.service

import com.example.domain.auth.Policies
import com.example.domain.auth.Policy
import com.example.growingshopauth.auth.dto.RoleRequest
import com.example.repository.auth.PolicyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PolicyService(
    private val policyRepository: PolicyRepository
) {

    fun findAll(): Policies = Policies(policyRepository.findAll())

    @Transactional
    fun create(req: RoleRequest.CreatePolicy): Policy = policyRepository.save(req.toEntity())

    @Transactional
    fun delete(id: Long) {
        policyRepository.deleteById(id)
    }
}
