package com.example.growingshopauth.config.routing

import org.springframework.cloud.gateway.route.builder.BooleanSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.http.HttpMethod

abstract class AllowRoutingConfig(
    private val allowPath: Map<HttpMethod?, List<String>>
) {
    fun allowPathPredicate(spec: PredicateSpec): BooleanSpec.BooleanOpSpec {
        return spec.predicate { exchange ->
            val req = exchange.request

            for ((method, path) in allowPath.entries) {
                val methodPredicate = method?.let {
                    req.method.equals(it)
                } ?: true
                val pathPredicate = path.contains(req.path.toString())

                if (methodPredicate && pathPredicate) return@predicate false
            }

            true
        }.and()
    }
}
