package com.example.growingshopauth.config.security

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
class SecurityConfig(
    private val securityContextRepository: SecurityContextRepository,
    context: ApplicationContext,
) {
    // TODO - method 로 분리하는 로직도 필요
    private val allowPaths = listOf("/auth/login", "/auth/join")

    private val authModulePaths =
        context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
            .handlerMethods.keys.asSequence()
            .flatMap { it.directPaths }
            .filter { it.isNotBlank() && !allowPaths.contains(it) }
            .toList()

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
            .pathMatchers(*authModulePaths.toTypedArray()).authenticated()
            .anyExchange().permitAll()
            .and()

            .formLogin().disable()
            .headers().frameOptions().disable()
            .and().build()
    }
}
