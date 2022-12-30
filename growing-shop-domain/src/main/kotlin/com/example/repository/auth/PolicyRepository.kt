package com.example.repository.auth

import com.example.domain.auth.Policy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PolicyRepository : JpaRepository<Policy, Long>
