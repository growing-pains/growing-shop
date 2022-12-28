package com.example.growingshopauth.user.repository

import com.example.growingshopauth.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUsersByLoginId(loginId: String): Optional<User>
}
