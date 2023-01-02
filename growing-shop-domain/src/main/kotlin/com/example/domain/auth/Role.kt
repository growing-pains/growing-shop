package com.example.domain.auth

import com.example.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

@Entity
class Role(
    @Column(nullable = false, unique = true)
    val name: String,

    policies: Policies = Policies()
) : BaseEntity() {
    @Embedded
    var policies: Policies = policies
        protected set

    fun changePolicies(target: Policies) {
        this.policies = target
    }
}
