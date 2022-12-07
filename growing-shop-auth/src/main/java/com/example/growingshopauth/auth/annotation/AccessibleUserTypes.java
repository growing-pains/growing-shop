package com.example.growingshopauth.auth.annotation;

import com.example.growingshopauth.user.domain.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessibleUserTypes {
    UserType[] value();
}