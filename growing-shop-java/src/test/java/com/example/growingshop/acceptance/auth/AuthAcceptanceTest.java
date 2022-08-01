package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.AcceptanceTest;
import com.example.growingshop.domain.auth.dto.AuthRequest;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/auth";

    private String name = "신규유저";
    private String mobile = "01000000000";
    private String email = "growing-shop@growing.shop";
    private String loginId = "growing-shop";
    private String password = "1234";

    @Test
    void 새로_회원가입하면_정상_가입되어야_한다() {
        // given
        AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
                .name(name)
                .mobile(mobile)
                .email(email)
                .loginId(loginId)
                .password(password)
                .build();

        // when
        ExtractableResponse<Response> response = defaultRequester.request(PATH + "/join", Method.POST, new HashMap<>(), join);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    void 존재하는_아이디로_중복_회원가입_시_회원가입에_실패해야_한다() {
        // given
        AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
                .name(name)
                .mobile(mobile)
                .email(email)
                .loginId(loginId)
                .password(password)
                .build();
        failRequester.request(PATH + "/join", Method.POST, new HashMap<>(), join);

        // when
        ExtractableResponse<Response> response = defaultRequester.request(PATH + "/join", Method.POST, new HashMap<>(), join);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void 등록된_사용자로_정상_로그인_시_토큰이_발급되어야_한다() {
        // given
        AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
                .name(name)
                .mobile(mobile)
                .email(email)
                .loginId(loginId)
                .password(password)
                .build();
        AuthRequest.LoginReq login = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(password)
                .build();
        defaultRequester.request(PATH + "/join", Method.POST, new HashMap<>(), join);

        // when
        ExtractableResponse<Response> response = defaultRequester.request(PATH + "/login", Method.POST, new HashMap<>(), login);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.jsonPath().getString("token")).isNotNull();
    }

    @Test
    void 존재하지_않는_회원이나_잘못된_패스워드로_로그인을_시도하면_인증이_실패되어야_한다() {
        // given
        AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
                .name(name)
                .mobile(mobile)
                .email(email)
                .loginId(loginId)
                .password(password)
                .build();
        AuthRequest.LoginReq invalidPasswordLogin = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(password + "wrong")
                .build();
        AuthRequest.LoginReq nonExistentUserLogin = AuthRequest.LoginReq.builder()
                .loginId(loginId + "other")
                .password(password)
                .build();
        defaultRequester.request(PATH + "/join", Method.POST, new HashMap<>(), join);

        // when
        ExtractableResponse<Response> invalidPasswordResponse =
                failRequester.request(PATH + "/login", Method.POST, new HashMap<>(), invalidPasswordLogin);
        ExtractableResponse<Response> nonexistentUserResponse =
                failRequester.request(PATH + "/login", Method.POST, new HashMap<>(), nonExistentUserLogin);

        // then
        assertThat(invalidPasswordResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
        assertThat(nonexistentUserResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }
}
