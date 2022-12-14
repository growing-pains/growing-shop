package com.example.growingshopauth.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
class SecurityConfig(
    private val securityContextRepository: SecurityContextRepository
) {
    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.cors().and().csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint { exchange, _ ->
                Mono.fromRunnable {
                    exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                }
            }.and()
            .securityContextRepository(securityContextRepository)
            .authorizeExchange()

            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .anyExchange().authenticated()
            .and()

            .formLogin().disable()
            .headers().frameOptions().disable()
            .and().build()

    }
}
