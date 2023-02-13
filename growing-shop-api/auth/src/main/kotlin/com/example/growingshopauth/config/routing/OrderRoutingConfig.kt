package com.example.growingshopauth.config.routing

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
class OrderRoutingConfig(
    @Value("\${growing-shop.endpoints.order}")
    private val uri: String,
    @Value("\${growing-shop.cloud-gateway-predicate.order}")
    private val predicate: String
) {

    @Bean
    @Profile("local")
    fun localOrderRoute(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes().route("order") {
            it.header(predicate)
                .filters { filterSpec ->
                    filterSpec.removeRequestHeader(predicate)
                }.uri(uri)
        }.build()
    }
}
