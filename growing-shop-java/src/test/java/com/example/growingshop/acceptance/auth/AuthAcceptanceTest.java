package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.AcceptanceTest;
import com.example.growingshop.acceptance.restDocs.request.auth.JoinRequester;
import com.example.growingshop.acceptance.restDocs.request.auth.LoginRequester;
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
        JoinRequester joinRequester = new JoinRequester();

        // when
        ExtractableResponse<Response> response = joinRequester.successRequestWithDocs();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    void 존재하는_아이디로_중복_회원가입_시_회원가입에_실패해야_한다() {
        // given
        JoinRequester joinRequester = new JoinRequester();
        joinRequester.successRequestWithDocs();

        // when
        ExtractableResponse<Response> response = joinRequester.failRequestWithDocs();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void 등록된_사용자로_정상_로그인_시_토큰이_발급되어야_한다() {
        // given
        JoinRequester joinRequester = new JoinRequester();
        LoginRequester loginRequester = new LoginRequester();
        joinRequester.successRequest();

        // when
        ExtractableResponse<Response> response = loginRequester.successRequestWithDocs();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.jsonPath().getString("token")).isNotNull();
    }

    @Test
    void 존재하지_않는_회원이나_잘못된_패스워드로_로그인을_시도하면_인증이_실패되어야_한다() {
        // given
        JoinRequester joinRequester = new JoinRequester();
        LoginRequester loginRequester = new LoginRequester();
        joinRequester.successRequest();

        // when
        ExtractableResponse<Response> response = loginRequester.failRequestWithDocs();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }
}
