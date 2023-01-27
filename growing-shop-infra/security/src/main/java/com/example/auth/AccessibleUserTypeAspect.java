package com.example.auth;

import com.example.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessibleUserTypeAspect {

    @Before("@annotation(AccessibleUserTypes)")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AccessibleUserTypes p = signature.getMethod().getAnnotation(AccessibleUserTypes.class);

        User loginUser = LoginUser.getUserInSecurityContext();

        if (isNotAllowUserType(p, loginUser)) {
            throw new AccessDeniedException("허용하지 않는 접근입니다.");
        }
    }

    private boolean isNotAllowUserType(AccessibleUserTypes types, User user) {
        return Arrays.stream(types.value())
                .noneMatch(type -> type == user.getType());
    }
}
