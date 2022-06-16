package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    void 새로_회원가입하면_정상_가입되어야_한다() {
        // given
        String name = "신규유저";
        String mobile = "01000000000";
        String email = "growing-ship@growing.shop";
        String loginId = "growing-ship";
        String password = "1234";

        // when
        ExtractableResponse<Response> response = AuthRestDocsRequestHelper.joinRequest(
                defaultSpec, name, mobile, email, loginId, password
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
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
        ExtractableResponse<Response> response = AuthRestDocsRequestHelper.joinRequest(
                failResponseSpec, name, mobile, email, loginId, password
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void 등록된_사용자로_정상_로그인_시_토큰이_발급되어야_한다() {
        // given
        String name = "신규유저";
        String mobile = "01000000000";
        String email = "newbie@test.com";
        String loginId = "newbie3";
        String password = "1234";
        AuthRestDocsRequestHelper.joinRequest(
                defaultSpec, name, mobile, email, loginId, password
        );

        // when
        ExtractableResponse<Response> response = AuthRestDocsRequestHelper.loginRequest(defaultSpec, loginId, password);

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
        ExtractableResponse<Response> invalidPasswordResponse = AuthRestDocsRequestHelper
                .loginRequest(failResponseSpec, loginId, password + "other");
        ExtractableResponse<Response> nonexistentUserResponse = AuthRestDocsRequestHelper
                .loginRequest(failResponseSpec, loginId + "other", password);

        // then
        assertThat(invalidPasswordResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
        assertThat(nonexistentUserResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }
}
