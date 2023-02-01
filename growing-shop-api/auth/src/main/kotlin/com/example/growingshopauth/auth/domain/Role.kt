package com.example.growingshopauth.auth.domain

import com.example.domain.auth.Policies
import com.example.domain.auth.Role

fun Role.changePolicies(target: Policies) {
    this.policies = target
}
