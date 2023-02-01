package com.example.auth;

import com.example.domain.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUser {
    public static User getUserInSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            throw new AccessDeniedException("유저 정보가 없습니다.");
        }
        if (!(principal instanceof User)) {
            throw new AccessDeniedException("로그인 된 유저 정보의 데이터가 잘못되었습니다");
        }

        return (User) principal;
    }
}
