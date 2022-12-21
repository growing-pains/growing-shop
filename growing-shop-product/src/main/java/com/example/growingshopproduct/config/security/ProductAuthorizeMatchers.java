package com.example.growingshopproduct.config.security;

import com.example.growingshopcommon.config.security.AuthorizeMatchers;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry;
import org.springframework.stereotype.Component;

@Component
public class ProductAuthorizeMatchers implements AuthorizeMatchers {
    @Override
    public AuthorizationManagerRequestMatcherRegistry configMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        return registry
                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                .anyRequest().authenticated();
    }
}
