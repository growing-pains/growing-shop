package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthorizeMatchers authorizeMatchers;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return authorizeMatchers.configMatchers(
                http.cors().and().csrf().disable()
                        .formLogin().disable()
                        .headers().frameOptions().disable()
                        .and()
                        .authorizeHttpRequests()
        ).and().build();
    }
}
