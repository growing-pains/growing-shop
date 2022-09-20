package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.domain.Policies;
import com.example.growingshop.domain.auth.domain.Policy;
import com.example.growingshop.domain.auth.dto.RoleRequest;
import com.example.growingshop.domain.auth.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyService {
    private final PolicyRepository policyRepository;

    public Policies findAll() {
        return new Policies(policyRepository.findAll());
    }

    @Transactional
    public Policy create(RoleRequest.CreatePolicy req) {
        return policyRepository.save(req.toEntity());
    }

    @Transactional
    public void delete(Long id) {
        policyRepository.deleteById(id);
    }
}
