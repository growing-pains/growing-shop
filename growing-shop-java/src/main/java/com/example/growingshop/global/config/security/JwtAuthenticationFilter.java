package com.example.growingshop.global.config.security;

import com.example.growingshop.global.error.exception.InvalidJwtTokenException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String userId = getUserIdFromRequestInJwt(request);
            if (StringUtils.hasText(userId)) {
                UserAuthentication authentication = new UserAuthentication(userId, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("인증 도중에 문제가 발생하였습니다.", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }

        filterChain.doFilter(request, response);
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
}
