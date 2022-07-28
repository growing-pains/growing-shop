package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.domain.Privileges;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.domain.Roles;
import com.example.growingshop.domain.auth.dto.RoleRequest;
import com.example.growingshop.domain.auth.repository.PrivilegeRepository;
import com.example.growingshop.domain.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    public Roles findAll() {
        return new Roles(roleRepository.findAll());
    }

    public Role findByName(String name) {
        return roleRepository.getByName(name)
                .orElseThrow(() -> new IllegalArgumentException(name + " 에 해당하는 role 을 찾을 수 없습니다."));
    }

    @Transactional
    public Role create(RoleRequest.CreateRole req) {
        Privileges privileges = new Privileges(privilegeRepository.findAllById(req.getPrivileges()));

        return roleRepository.save(req.toEntity(privileges));
    }
}
