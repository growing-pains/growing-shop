package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.domain.Privilege;
import com.example.growingshop.domain.auth.domain.Privileges;
import com.example.growingshop.domain.auth.dto.RoleRequest;
import com.example.growingshop.domain.auth.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    public Privileges findAll() {
        return new Privileges(privilegeRepository.findAll());
    }

    public Privilege create(RoleRequest.CreatePrivilege req) {
        return privilegeRepository.save(req.toEntity());
    }
}
