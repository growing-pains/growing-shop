package com.example.growingshopauth.config.routing

import com.example.domain.user.User
import com.example.growingshopauth.config.error.exception.NotFoundUserException
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class GlobalUserContext(
    @Value("\${redis.auth-key}")
    private val redisKey: String,
    private val redissonClient: RedissonClient
) {
    private val userSession: RMapCache<String, User> = redissonClient.getMapCache(redisKey)

    fun userByKey(key: String): User {
        return userSession[key] ?: throw NotFoundUserException("유저를 찾을 수 없습니다.")
    }

//    companion object {
// TODO - reactive 학습 후 context 에 저장하여 request 별로 접근 가능하도록 적용 필요

//        private const val USER_CONTEXT_KEY = "userContext"
//
//        fun getUserContext(): Mono<User> {
////            return Mono.deferContextual {
////                it[USER_CONTEXT_KEY]
////            }
//            return Mono.deferContextual {
//                (it.get(ServerWebExchange::class.java).attributes[USER_CONTEXT_KEY] as User)
//                    .toMono()
//            }
//        }
//
//        fun setUserContext(user: User) {
//            Mono.empty<Void>().contextWrite {
//                it.put(USER_CONTEXT_KEY, user)
//            }.subscribe()
//        }
//    }
}
