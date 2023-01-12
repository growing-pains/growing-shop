package com.example.growingshopauth.auth.annotation

import com.example.domain.user.UserType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessibleUserTypes(
    vararg val value: UserType
)
