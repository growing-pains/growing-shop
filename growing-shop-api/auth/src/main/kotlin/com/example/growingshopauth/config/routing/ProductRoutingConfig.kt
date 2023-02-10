package com.example.growingshopauth.config.routing

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

@Component
class ProductRoutingConfig(
    @Value("\${growing-shop.endpoints.product}")
    private val uri: String,
    @Value("\${growing-shop.cloud-gateway-predicate.product}")
    private val predicate: String
) {

    private val allowPath: Map<HttpMethod?, List<String>> = mapOf(
        HttpMethod.GET to listOf("/products")
    )

    @Bean
    @Profile("local")
    fun localProductRoute(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes().route("product") {
            it.allowPathPredicate(allowPath).header(predicate)
                .filters { filterSpec ->
                    filterSpec.removeRequestHeader(predicate)
                }.uri(uri)
        }.build()
    }
}
