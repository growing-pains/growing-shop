package com.example.growingshopauth.auth.annotation;

import com.example.growingshopauth.user.domain.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
// TODO 모듈화 하면서 각 모듈에서 활용할 수 있는 방안 찾기
public @interface AccessibleUserTypes {
    UserType[] value();
}
