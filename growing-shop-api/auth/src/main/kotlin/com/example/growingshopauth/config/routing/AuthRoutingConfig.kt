package com.example.growingshopauth.config.routing

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod

@Configuration
class AuthRoutingConfig(
    @Value("\${growing-shop.endpoints.auth}")
    private val uri: String,
    @Value("\${growing-shop.cloud-gateway-predicate.auth}")
    private val predicate: String
) {

    private val allowPath: Map<HttpMethod?, List<String>> = mapOf(
        HttpMethod.POST to listOf("/auth/login", "/auth/join")
    )

    @Bean
    @Profile("local")
    fun localAuthRoute(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes().route("auth") {
            allowPathPredicate(it)
                .header("Proxy-To", predicate)
//                .or().path("/test/**").filters { filterSpec ->
//                    filterSpec.setRequestHeader("hello", "world")
//                        .rewritePath("/test", "")
//                }
                .uri(uri)
        }.build()
    }

    private fun allowPathPredicate(spec: PredicateSpec): PredicateSpec {
        allowPath.entries.forEach { (method, path) ->
            spec.not {
                spec.method(method).and().path(*path.toTypedArray())
            }
        }

        return spec
    }
}
