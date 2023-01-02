package com.example.growingshopauth.user.repository

import com.example.domain.user.User
import com.example.repository.user.UserRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExpandUserRepository : UserRepository {
    fun findUsersByLoginId(loginId: String): Optional<User>
}
