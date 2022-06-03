package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.helper.AuthRequestHelper;
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
        String name = "신규유저";
        String mobile = "01000000000";
        String email = "newbie@test.com";
        String loginId = "newbie";
        String password = "1234";

        // when
        ExtractableResponse<Response> response = AuthRequestHelper.joinRequest(
                name, mobile, email, loginId, password
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(response.jsonPath().getBoolean("state")).isTrue();
    }

    @Test
    void 존재하는_아이디로_중복_회원가입_시_회원가입에_실패해야_한다() {
        // given
        String name = "신규유저";
        String mobile = "01000000000";
        String email = "newbie@test.com";
        String loginId = "newbie2";
        String password = "1234";
        AuthRequestHelper.joinRequest(
                name, mobile, email, loginId, password
        );

        // when
        ExtractableResponse<Response> response = AuthRequestHelper.joinRequest(
                name, mobile, email, loginId, password
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        assertThat(response.jsonPath().getBoolean("state")).isFalse();
    }

    @Test
    void 등록된_사용자로_정상_로그인_시_토큰이_발급되어야_한다() {
        // given
        String name = "신규유저";
        String mobile = "01000000000";
        String email = "newbie@test.com";
        String loginId = "newbie3";
        String password = "1234";
        AuthRequestHelper.joinRequest(
                name, mobile, email, loginId, password
        );

        // when
        ExtractableResponse<Response> response = AuthRequestHelper.loginRequest(loginId, password);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.jsonPath().getString("token")).isNotNull();
    }

    @Test
    void 존재하지_않는_회원이나_잘못된_패스워드로_로그인을_시도하면_인증이_실패되어야_한다() {
        // given
        String name = "신규유저";
        String mobile = "01000000000";
        String email = "newbie@test.com";
        String loginId = "newbie4";
        String password = "1234";
        AuthRequestHelper.joinRequest(
                name, mobile, email, loginId, password
        );

        // when
        ExtractableResponse<Response> invalidPasswordResponse = AuthRequestHelper
                .loginRequest(loginId, password + "other");
        ExtractableResponse<Response> nonexistentUserResponse = AuthRequestHelper
                .loginRequest(loginId + "other", password);

        // then
        assertThat(invalidPasswordResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
        assertThat(nonexistentUserResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }
}
