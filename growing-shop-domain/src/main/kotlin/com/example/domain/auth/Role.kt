package com.example.domain.auth

import com.example.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

@Entity
class Role(
    @Column(nullable = false, unique = true)
    val name: String,

    @Embedded
    var policies: Policies = Policies()
) : BaseEntity()
