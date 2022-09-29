package com.example.growingshop.domain.auth.accessible;

import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.global.error.exception.NotAllowPathException;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUser {
    public static User getUserInSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            throw new NotAllowPathException("유저 정보가 없습니다.");
        }
        if (!(principal instanceof User)) {
            throw new NotAllowPathException("로그인 된 유저 정보의 데이터가 잘못되었습니다");
        }

        return (User) principal;
    }
}
