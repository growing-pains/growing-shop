package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.HttpMethod;
import com.example.growingshop.domain.auth.domain.Policies;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.service.PolicyService;
import com.example.growingshop.domain.auth.service.RoleService;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import com.example.growingshop.global.error.exception.NotAllowPathException;
import com.example.growingshop.global.error.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
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

        checkAccessiblePath(request, response);

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request) {
        try {
            String userId = getUserIdFromRequestInJwt(request);
            User user = userRepository.findUsersByLoginId(userId)
                    .orElseThrow(() -> new NotFoundUserException("유저 정보를 찾을 수 없습니다."));
            Role userTypeRole = roleService.findByName(user.getType().name());
            Authority authority = new Authority(user.getLoginId(), userTypeRole, user.getRoles());

            UserAuthentication authentication = new UserAuthentication(user, authority);
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

    private void checkAccessiblePath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

            if (isNotAccessiblePath(request.getRequestURI(), httpMethod)) {
                throw new IllegalAccessException("요청한 경로에 접근 할 수 없습니다.");
            }
        } catch (Exception ex) {
            logger.error("인증 도중에 문제가 발생하였습니다.", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }

    private boolean isNotAccessiblePath(String path, HttpMethod method) {
        Policies policies = policyService.findAll();

        if (policies.containPath(path)) {
            return !getUserAuthority().possibleAccess(path, method);
        }
        return false;
    }

    private Authority getUserAuthority() {
        Object authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new NotAllowPathException("요청에 대한 인증 정보가 없습니다.");
        }
        if (!(authentication instanceof UserAuthentication)) {
            throw new NotAllowPathException("로그인 된 유저 정보의 데이터가 잘못되었습니다");
        }

        GrantedAuthority authority = ((UserAuthentication) authentication).getAuthorities().iterator().next();

        if (!(authority instanceof Authority)) {
            throw new NotAllowPathException("로그인 된 유저의 인증 정보가 잘못되었습니다.");
        }

        return (Authority) authority;
    }
}
