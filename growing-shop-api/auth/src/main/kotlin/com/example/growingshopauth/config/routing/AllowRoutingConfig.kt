package com.example.growingshopauth.config.routing

import com.example.growingshopauth.config.routing.DefaultFilter.Companion.SKIP_CHECK_AUTH_KEY
import org.springframework.cloud.gateway.route.builder.BooleanSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.http.HttpMethod

fun PredicateSpec.allowPathPredicate(
    allowPath: Map<HttpMethod?, List<String>>,
    uri: String
): BooleanSpec.BooleanOpSpec {
    // TODO - security 모듈에 webflux, was 시큐리티 모두 수용 가능하게 변경 후, 각 서비스에서 allow path 에 대한 설정을 관리할 수 있도록 리팩토링 필요
    // 현재는 각 서비스에서 인증이 필요없는 path 를 설정하고, auth 모듈에서 추가적으로 설정해야 하므로 여러곳에서 설정 관리 및 누락될 가능성이 있음
    return this.predicate { exchange ->
        val req = exchange.request

        for ((method, path) in allowPath.entries) {
            val methodPredicate = method?.let {
                req.method.equals(it)
            } ?: true
            val pathPredicate = path.contains(req.path.toString())

            if (methodPredicate && pathPredicate) {
                this.alwaysTrue().filters { filterSpec ->
                    filterSpec.addRequestHeader(SKIP_CHECK_AUTH_KEY, "true")
                }

                return@predicate true
            }
        }

        false
    }.and()
}
