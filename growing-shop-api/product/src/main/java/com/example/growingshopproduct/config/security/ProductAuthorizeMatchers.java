package com.example.growingshopproduct.config.security;

import com.example.config.AuthorizeMatchers;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ProductAuthorizeMatchers implements AuthorizeMatchers {
    @Override
    public AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configMatchers(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry
    ) {
        return registry
                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                .anyRequest().authenticated();
    }
}
