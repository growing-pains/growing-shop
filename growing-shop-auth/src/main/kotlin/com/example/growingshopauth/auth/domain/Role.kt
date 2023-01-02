package com.example.growingshopauth.auth.domain

import com.example.growingshopcommon.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

@Entity
class Role(
    @Column(nullable = false, unique = true)
    val name: String,

    @Embedded
    var policies: Policies = Policies()
) : BaseEntity() {
    fun changePolicies(target: Policies) {
        this.policies = target
    }
}
