package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Cacheable(value = "#name")
    public Role findByName(String name) {
        return roleRepository.getByName(name)
                .orElseThrow(() -> new IllegalArgumentException(name + " 에 해당하는 role 을 찾을 수 없습니다."));
    }
}
