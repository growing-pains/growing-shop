package com.example.config;

import com.example.domain.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final RedissonClient redissonClient;

    @Value("${redis.auth-key}")
    private String redisKey;

    private RMapCache<String, User> userSession;

    @PostConstruct
    public void init() {
        userSession = redissonClient.getMapCache(redisKey);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        if (hasUUIDInHeader(request)) {
            setAuthentication(request);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request) {
        try {
            String uuid = request.getHeader(HttpHeaders.AUTHORIZATION);
            User user = userSession.get(uuid);

            if (user == null) throw new SecurityException("요청에 대한 유저 정보를 찾을 수 없습니다.");

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            logger.warn(ex);
        }
    }

    private boolean hasUUIDInHeader(HttpServletRequest request) {
        String uuid = request.getHeader(HttpHeaders.AUTHORIZATION);

        return StringUtils.hasText(uuid) && UUID_REGEX.matcher(uuid).matches();
    }
}
