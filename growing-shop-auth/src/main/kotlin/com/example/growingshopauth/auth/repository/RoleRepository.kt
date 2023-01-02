package com.example.growingshopauth.auth.repository

import com.example.growingshopauth.auth.domain.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RoleRepository: JpaRepository<Role, Long> {

    fun getByName(name: String): Optional<Role>
}
