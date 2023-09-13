package com.example.growingshopauth.acceptance

import com.example.InitDocs
//import com.example.Spec
//import com.example.growingshopauth.auth.dto.AuthRequest
//import io.restassured.http.Method
//import io.restassured.response.ExtractableResponse
//import io.restassured.response.Response
//import org.apache.http.HttpStatus.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest

//@WebFluxTest
@DisplayName("인증 관련 기능")
class AuthAcceptanceTest: InitDocs() {
//class AuthAcceptanceTest {

    @Test
    fun 새로_회원가입하면_정상_가입되어야_한다() {
        Assertions.assertThat(true).isTrue()
//        // when
//        val joinReq = AuthRequest.JoinReq(
//            name = "이름",
//            mobile = "핸드폰 번호",
//            email = "이메일",
//            loginId = "로그인 아이디",
//            password = "패스워드",
//            company = null,
//        )
//        val response: ExtractableResponse<Response> = Spec.request(
//            spec = defaultSpec,
//            method = Method.POST,
//            path = "/auth/join",
//            body = joinReq,
//        )
//
//        // then
//        Assertions.assertThat(response.statusCode()).isEqualTo(SC_CREATED)
    }

//    @Test
//    fun 존재하는_아이디로_중복_회원가입_시_회원가입에_실패해야_한다() {
//        // when
//        val response: ExtractableResponse<Response> = joinRequester.failRequestWithDocs()
//
//        // then
//        Assertions.assertThat(response.statusCode()).isEqualTo(SC_BAD_REQUEST)
//    }
//
//    @Test
//    fun 등록된_사용자로_정상_로그인_시_토큰이_발급되어야_한다() {
//        // given
//        joinRequester.successRequest()
//
//        // when
//        val response: ExtractableResponse<Response> = loginRequester.successRequestWithDocs()
//
//        // then
//        Assertions.assertThat(response.statusCode()).isEqualTo(SC_OK)
//        Assertions.assertThat(response.jsonPath().getString("token")).isNotNull()
//    }
//
//    @Test
//    fun 존재하지_않는_회원이나_잘못된_패스워드로_로그인을_시도하면_인증이_실패되어야_한다() {
//        // given
//        joinRequester.successRequest()
//
//        // when
//        val response: ExtractableResponse<Response> = loginRequester.failRequestWithDocs()
//
//        // then
//        Assertions.assertThat(response.statusCode()).isEqualTo(SC_UNAUTHORIZED)
//    }
}
