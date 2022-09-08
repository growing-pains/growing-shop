package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.Policies;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.dto.Authorities;
import com.example.growingshop.domain.auth.dto.Authority;
import com.example.growingshop.domain.auth.service.PolicyService;
import com.example.growingshop.domain.auth.service.RoleService;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import com.example.growingshop.global.error.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final UserRepository userRepository;
    private final PolicyService policyService;
    private final RoleService roleService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        if (hasTokenInHeader(request)) {
            setAuthentication(request);
        }

        Policies policies = policyService.findAll();

        if (policies.containPath(request.getRequestURI())) {
            accessiblePath(request.getRequestURI(), response);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request) {
        try {
            String userId = getUserIdFromRequestInJwt(request);
            User user = userRepository.findUsersByLoginId(userId)
                    .orElseThrow(() -> new NotFoundUserException("유저 정보를 찾을 수 없습니다."));
            Role userTypeRole = roleService.findByName(user.getType().name());
            List<Authority> authorities = user.getRoles()
                    .combineWithUserDefaultRole(userTypeRole)
                    .getGrantedAuthorities();

            UserAuthentication authentication = new UserAuthentication(user, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            logger.warn(ex);
        }
    }

    private boolean hasTokenInHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);

        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX);
    }

    private String getUserIdFromRequestInJwt(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER)
                .substring(AUTH_HEADER_PREFIX.length());
        return JwtTokenProvider.getUserIdFromJwt(token);
    }

    private void accessiblePath(String path, HttpServletResponse response) throws IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal() == null) {
                throw new IllegalArgumentException("요청에 대한 유저 정보가 없습니다.");
            }
            if (isNotAccessiblePath(authentication, path)) {
                throw new IllegalAccessException("요청한 경로에 접근 할 수 없습니다.");
            }
        } catch (Exception ex) {
            logger.error("인증 도중에 문제가 발생하였습니다.", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }

    private boolean isNotAccessiblePath(Authentication authentication, String path) {
        Authorities authorities = new Authorities((List<Authority>) authentication.getAuthorities());
        // TODO - authorities 에 대한 타입을 제대로 활용할 수 있게 변경 필요
        // -> unchecked cast 해결하기

        return authorities.isAllowAccessPath(path);
    }
}
