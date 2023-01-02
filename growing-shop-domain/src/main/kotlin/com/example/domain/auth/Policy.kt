package com.example.domain.auth

import com.example.domain.common.BaseEntity
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
) : BaseEntity()
