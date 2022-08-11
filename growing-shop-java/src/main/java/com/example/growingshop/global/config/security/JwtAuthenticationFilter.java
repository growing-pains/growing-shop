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
            logger.error("Could not set user authentication in security context", ex);
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
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX)) {
            return bearerToken.substring(AUTH_HEADER_PREFIX.length());
        }

        throw new InvalidJwtTokenException("Need to Jwt Token with Bearer");
    }
}
