package com.example.growingshop.domain.auth.repository;

import com.example.growingshop.domain.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> getByName(String name);
}
