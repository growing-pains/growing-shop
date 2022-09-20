package com.example.growingshop.global.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    public UserAuthentication(Object principal, GrantedAuthority authority) {
        super(principal, null, Collections.singletonList(authority));
    }
}
