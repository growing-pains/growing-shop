package com.example.growingshop.domain.auth.accessible;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("T(com.example.growingshop.domain.user.domain.UserType).NORMAL == authentication.principal.type")
public @interface NormalUser {
}
