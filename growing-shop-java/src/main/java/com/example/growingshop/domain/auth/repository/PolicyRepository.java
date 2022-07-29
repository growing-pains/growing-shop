package com.example.growingshop.domain.auth.repository;

import com.example.growingshop.domain.auth.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
