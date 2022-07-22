package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.Privileges;
import com.example.growingshop.domain.auth.error.NotFoundUserException;
import com.example.growingshop.domain.auth.service.PrivilegeService;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AccessiblePath {
    private final UserRepository userRepository;
    private final PrivilegeService privilegeService;

    public boolean check(HttpServletRequest request, Authentication authentication) {
        Privileges privileges = privilegeService.findAll();

        if (privileges.containPath(request.getPathInfo())) {
            User user = getUserByAuthentication(authentication);

            return user.getRoles()
                    .getGrantedAuthorities()
                    .isAllowAccessPath(request.getPathInfo());
        }
        return true;
    }

    private User getUserByAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() == null) {
            throw new IllegalStateException("유저 정보가 없습니다.");
        }

        String loginId = (String) authentication.getPrincipal();
        return userRepository.findUsersByLoginId(loginId)
                .orElseThrow(() -> new NotFoundUserException("유저 정보를 찾을 수 없습니다."));
    }
}
