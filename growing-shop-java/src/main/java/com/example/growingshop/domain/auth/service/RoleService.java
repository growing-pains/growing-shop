package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.domain.Policies;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.domain.Roles;
import com.example.growingshop.domain.auth.dto.RoleRequest;
import com.example.growingshop.domain.auth.repository.PolicyRepository;
import com.example.growingshop.domain.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final PolicyRepository policyRepository;

    public Roles findAll() {
        return new Roles(roleRepository.findAll());
    }

    public Role getById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 role 을 찾을 수 없습니다."));
    }

    public Role findByName(String name) {
        return roleRepository.getByName(name)
                .orElseThrow(() -> new IllegalArgumentException(name + " 에 해당하는 role 을 찾을 수 없습니다."));
    }

    @Transactional
    public Role create(RoleRequest.CreateRole req) {
        Policies policies = new Policies(policyRepository.findAllById(req.getPolicies()));

        return roleRepository.save(req.toEntity(policies));
    }

    @Transactional
    public void changePolicies(RoleRequest.ChangeRolePolicies req) {
        Role role = getById(req.getRole());
        Policies policies = new Policies(policyRepository.findAllById(req.getPolicies()));

        role.changePolicies(policies);
    }
}
