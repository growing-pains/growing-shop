package com.example.growingshopauth.config.routing

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping

@Component
class AuthRoutingConfig(
    @Value("\${growing-shop.endpoints.auth}")
    private val uri: String,
    context: ApplicationContext
) {
    private val allowPath: Map<HttpMethod?, List<String>> = mapOf(
        HttpMethod.POST to listOf("/auth/login", "/auth/join")
    )

    private val authModulePaths =
        context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
            .handlerMethods.keys.asSequence()
            .flatMap { it.directPaths }
            .filter { it.isNotBlank() }
            .toList()

    @Bean
    @Profile("local")
    fun localAuthRoute(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes().route("auth") {
            it.allowPathPredicate(allowPath, uri).path(*authModulePaths.toTypedArray()).uri(uri)
        }.build()
    }
}
