package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.domain.Privileges;
import com.example.growingshop.domain.auth.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    @Cacheable(value = "privileges", unless = "#result.isEmpty()")
    public Privileges findAll() {
        return new Privileges(privilegeRepository.findAll());
    }
}
