package com.example.growingshopauth.config.routing

import com.example.domain.user.User
import reactor.core.publisher.Mono

class GlobalUserContext {
    companion object {
        private const val USER_CONTEXT_KEY = "userContext"

        fun getUserContext(): Mono<User> {
            return Mono.deferContextual {
                it[USER_CONTEXT_KEY]
            }
        }

        fun setUserContext(user: User): Mono<Void> {
            return Mono.empty<Void>().contextWrite {
                it.put(USER_CONTEXT_KEY, user)
            }
        }
    }
}
