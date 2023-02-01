package com.example.growingshopauth.config.routing

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
class ProductRoutingConfig(
    @Value("\${growing-shop.endpoints.product}")
    private val uri: String,
    @Value("\${growing-shop.cloud-gateway-predicate.product}")
    private val predicate: String
) {
    @Bean
    @Profile("local")
    fun localAuthRoute(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes().route("product") {
            it.header(LOCAL_PREDICATE_BY, predicate)
                .filters { filterSpec ->
                    filterSpec.removeRequestHeader("Proxy-To")
                }.uri(uri)
        }.build()
    }

    companion object {
        private const val LOCAL_PREDICATE_BY = "Proxy-To"
    }
}
