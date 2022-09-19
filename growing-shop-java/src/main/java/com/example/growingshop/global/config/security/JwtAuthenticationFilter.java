package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.HttpMethod;
import com.example.growingshop.domain.auth.domain.Policies;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.service.PolicyService;
import com.example.growingshop.domain.auth.service.RoleService;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import com.example.growingshop.global.error.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import com.example.growingshop.global.error.exception.InvalidJwtTokenException;
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
        Policies policies = policyService.findAll();

        if (policies.containPath(request.getRequestURI())) {
            setAuthentication(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String userId = getUserIdFromRequestInJwt(request);
            User user = userRepository.findUsersByLoginId(userId)
                    .orElseThrow(() -> new NotFoundUserException("유저 정보를 찾을 수 없습니다."));
            HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

            if (isAccessiblePath(user, request.getRequestURI(), httpMethod)) {
                UserAuthentication authentication = new UserAuthentication(userId, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                return;
            }

            throw new InvalidJwtTokenException("요청한 Path 에 접근할 권한이 없습니다.");
        } catch (Exception ex) {
            logger.error("인증 도중에 문제가 발생하였습니다.", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }

    private String getUserIdFromRequestInJwt(HttpServletRequest request) {
        String token = getJwtTokenInRequest(request);
        return JwtTokenProvider.getUserIdFromJwt(token);
    }

    private String getJwtTokenInRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);
        if (validBearerToken(bearerToken)) {
            return bearerToken.substring(AUTH_HEADER_PREFIX.length());
        }

        throw new InvalidJwtTokenException("헤더에 Bearer JWT 토큰 정보가 없습니다.");
    }

    private boolean validBearerToken(String bearerToken) {
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX);
    }

    private boolean isAccessiblePath(User user, String path, HttpMethod httpMethod) {
        Role userTypeRole = roleService.findByName(user.getType().name());
        Authority authority = new Authority(user.getLoginId(), userTypeRole, user.getRoles());

        return authority.possibleAccess(path, httpMethod);
    }
}
