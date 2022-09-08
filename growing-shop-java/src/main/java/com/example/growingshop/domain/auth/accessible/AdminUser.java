package com.example.growingshop.domain.auth.accessible;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("T(com.example.growingshop.domain.user.domain.UserType).ADMIN == authentication.principal.type")
public @interface AdminUser {
}
// TODO - user type 에 대한 필터링 annotation 구현하기
