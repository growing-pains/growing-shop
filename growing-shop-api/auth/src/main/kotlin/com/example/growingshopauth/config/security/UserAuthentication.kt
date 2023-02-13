package com.example.growingshopauth.config.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UserAuthentication(principal: Any, authority: GrantedAuthority) :
    UsernamePasswordAuthenticationToken(principal, null, listOf(authority))

