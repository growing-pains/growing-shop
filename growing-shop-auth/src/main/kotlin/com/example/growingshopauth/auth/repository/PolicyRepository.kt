package com.example.growingshopauth.auth.repository

import com.example.growingshopauth.auth.domain.Policy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PolicyRepository : JpaRepository<Policy, Long> {
}
