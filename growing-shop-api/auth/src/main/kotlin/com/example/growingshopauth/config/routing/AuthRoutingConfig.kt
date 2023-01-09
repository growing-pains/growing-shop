package com.example.growingshopauth.config.routing

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

@Component
class AuthRoutingConfig(
    @Value("\${growing-shop.endpoints.auth}")
    private val uri: String,
    @Value("\${growing-shop.cloud-gateway-predicate.auth}")
    private val predicate: String
): AllowRoutingConfig(
    mapOf(
        HttpMethod.POST to listOf("/auth/login", "/auth/join"),
        HttpMethod.GET to listOf("/auth/test"), // for webflux test. will be deleted
    )
) {
    @Bean
    @Profile("local")
    fun localAuthRoute(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes().route("auth") {
            allowPathPredicate(it).header(LOCAL_PREDICATE_BY, predicate)
                .filters { filterSpec ->
                    filterSpec.removeRequestHeader("Proxy-To")
                }.uri(uri)
        }.build()
    }

    companion object {
        private const val LOCAL_PREDICATE_BY = "Proxy-To"
    }
}
