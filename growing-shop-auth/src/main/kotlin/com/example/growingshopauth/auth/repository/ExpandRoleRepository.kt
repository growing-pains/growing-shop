package com.example.growingshopauth.auth.repository

import com.example.domain.auth.Role
import com.example.repository.auth.RoleRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExpandRoleRepository : RoleRepository {
    fun getByName(name: String): Optional<Role>
}
