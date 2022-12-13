package com.example.growingshopauth.auth.api

import com.example.growingshopauth.auth.dto.AuthRequest
import com.example.growingshopauth.auth.dto.AuthResponse
import com.example.growingshopauth.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
//    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody @Validated login: AuthRequest.LoginReq): ResponseEntity<AuthResponse.TokenRes> {
//        return ResponseEntity<>(jwtTokenProvider.generateToken(login), HttpStatus.OK)
        TODO("security 설정 후 login api 구현")
    }

    @PostMapping("/join")
    fun join(@RequestBody @Validated join: AuthRequest.JoinReq): ResponseEntity<Any> {
        userService.joinUser(join)

        return ResponseEntity(HttpStatus.CREATED)
    }
}
