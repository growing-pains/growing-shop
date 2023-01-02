package com.example.growingshopauth.auth.domain

import com.example.growingshopcommon.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.util.regex.Pattern

@Entity
class Policy(
    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val path: String,

    @Enumerated(EnumType.STRING)
    val method: HttpMethod,

    var description: String = ""
) : BaseEntity() {
    companion object {
        private val PATH_REGEX = Pattern.compile("^[\\/]+[\\w-/]+$", Pattern.DOTALL)

        fun validatePath(path: String) {
            if (!PATH_REGEX.matcher(path).matches()) throw IllegalArgumentException("유효하지 않은 url path 입니다.")
        }
    }
}
