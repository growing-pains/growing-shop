package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.helper.AuthRequestHelper;
import com.example.growingshop.domain.auth.dto.AuthRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
public class AuthAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }
    }

    @Test
    void 새로_회원가입하면_정상_가입되어야_한다() {
        // given
        AuthRequest.JoinReq request = AuthRequest.JoinReq.builder()
                .name("신규유저")
                .mobile("01000000000")
                .email("newbie@test.com")
                .loginId("newbie")
                .password("1234")
                .build();

        // when
        ExtractableResponse<Response> response = AuthRequestHelper.joinRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(response.jsonPath().getBoolean("state")).isTrue();
    }

    @Test
    void 존재하는_아이디로_중복_회원가입_시_회원가입에_실패해야_한다() {
        // given
        AuthRequestHelper.joinRequest(
                AuthRequest.JoinReq.builder()
                        .name("신규유저")
                        .mobile("01000000000")
                        .email("newbie@test.com")
                        .loginId("newbie")
                        .password("1234")
                        .build()
        );

        // when
        ExtractableResponse<Response> response = AuthRequestHelper.joinRequest(
                AuthRequest.JoinReq.builder()
                        .name("신규유저")
                        .mobile("01000000000")
                        .email("newbie@test.com")
                        .loginId("newbie")
                        .password("1234")
                        .build()
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        assertThat(response.jsonPath().getBoolean("state")).isFalse();
    }

    @Test
    void 등록된_사용자로_정상_로그인_시_토큰이_발급되어야_한다() {
        // given
        AuthRequestHelper.joinRequest(
                AuthRequest.JoinReq.builder()
                        .name("신규유저")
                        .mobile("01000000000")
                        .email("newbie@test.com")
                        .loginId("newbie")
                        .password("1234")
                        .build()
        );
        AuthRequest.LoginReq request = AuthRequest.LoginReq.builder()
                .loginId("newbie")
                .password("1234")
                .build();

        // when
        ExtractableResponse<Response> response = AuthRequestHelper.loginRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.jsonPath().getString("token")).isNotNull();
    }

    @Test
    void 존재하지_않는_회원이나_잘못된_패스워드로_로그인을_시도하면_인증이_실패되어야_한다() {
        // given
        AuthRequestHelper.joinRequest(
                AuthRequest.JoinReq.builder()
                        .name("신규유저")
                        .mobile("01000000000")
                        .email("newbie@test.com")
                        .loginId("newbie")
                        .password("1234")
                        .build()
        );
        AuthRequest.LoginReq loginByInvalidPassword = AuthRequest.LoginReq.builder()
                .loginId("newbie")
                .password("12345")
                .build();
        AuthRequest.LoginReq loginByNonexistentUser = AuthRequest.LoginReq.builder()
                .loginId("newbie1")
                .password("1234")
                .build();

        // when
        ExtractableResponse<Response> invalidPasswordResponse = AuthRequestHelper.loginRequest(loginByInvalidPassword);
        ExtractableResponse<Response> nonexistentUserResponse = AuthRequestHelper.loginRequest(loginByInvalidPassword);

        // then
        assertThat(invalidPasswordResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
        assertThat(nonexistentUserResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }
}
