package com.example.growingshopauth.auth.annotation

import com.example.domain.user.User
import com.example.growingshopauth.config.error.exception.NotAllowPathException
import com.example.growingshopauth.config.routing.GlobalUserContext
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class AccessibleUserTypeAspect(
    private val globalUserContext: GlobalUserContext
) {

    @Before("@annotation(com.example.growingshopauth.auth.annotation.AccessibleUserTypes)")
    fun before(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature
        val p = signature.method.getAnnotation(
            AccessibleUserTypes::class.java
        )
        val loginUser = globalUserContext.userByKey("")
        // 음..... 전역 context 에 저장해야 하는데, 추후 적용합시다....
        if (isNotAllowUserType(p, loginUser)) {
            throw NotAllowPathException("허용하지 않는 접근입니다.")
        }
    }

    private fun isNotAllowUserType(types: AccessibleUserTypes, user: User): Boolean {
        return types.value.none { type -> type == user.type }
    }
}
