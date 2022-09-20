package com.example.growingshop.domain.auth.accessible;

import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.global.error.exception.NotAllowPathException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AccessibleUserTypeAspect {

    @Before("@annotation(AccessibleUserTypes)")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AccessibleUserTypes p = signature.getMethod().getAnnotation(AccessibleUserTypes.class);

        User loginUser = getUserInSecurityContext();

        if (isNotAllowUserType(p, loginUser)) {
            throw new NotAllowPathException("허용하지 않는 접근입니다.");
        }
    }

    private User getUserInSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            throw new NotAllowPathException("유저 정보가 없습니다.");
        }
        if (!(principal instanceof User)) {
            throw new NotAllowPathException("로그인 된 유저 정보의 데이터가 잘못되었습니다");
        }

        return (User) principal;
    }

    private boolean isNotAllowUserType(AccessibleUserTypes types, User user) {
        return !Arrays.asList(types.value()).stream().anyMatch(type -> type == user.getType());
    }
}
