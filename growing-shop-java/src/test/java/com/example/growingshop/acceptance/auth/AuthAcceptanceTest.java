package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.AcceptanceTest;
import com.example.growingshop.acceptance.restDocs.BodyDescription;
import com.example.growingshop.acceptance.restDocs.Description;
import com.example.growingshop.acceptance.restDocs.Requester;
import com.example.growingshop.domain.auth.dto.AuthRequest;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/auth";

    private String name = "신규유저";
    private String mobile = "01000000000";
    private String email = "growing-shop@growing.shop";
    private String loginId = "growing-shop";
    private String password = "1234";
    private AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
            .name(name)
            .mobile(mobile)
            .email(email)
            .loginId(loginId)
            .password(password)
            .build();
    private Description joinDescription = new BodyDescription(
            "auth/join",
            Map.of(
                    "name", "이름",
                    "mobile", "핸드폰 번호",
                    "email", "이메일",
                    "loginId", "로그인 아이디",
                    "password", "패스워드",

                    "company", "회사 정보",
                    "joinPassword", "회원가입 비밀번호"
                    // 일부 필드 제외하고 description 을 할 수 있는 방법 찾기
            )
    );
    private Description loginDescription = new BodyDescription(
            "auth/login",
            Map.of(
                    "loginId", "로그인 아이디",
                    "password", "패스워드"
            )
    );

    @Test
    void 새로_회원가입하면_정상_가입되어야_한다() {
        // given
        Requester requester = new Requester.RequesterBuilder(PATH + "/join", Method.POST)
                .spec(defaultSpec)
                .body(join)
                .description(joinDescription)
                .build();


        // when
        ExtractableResponse<Response> response = requester.request();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    void 존재하는_아이디로_중복_회원가입_시_회원가입에_실패해야_한다() {
        // given
        Requester requester = new Requester.RequesterBuilder(PATH + "/join", Method.POST)
                .spec(failResponseSpec)
                .body(join)
                .build();
        requester.request();

        // when
        ExtractableResponse<Response> response = requester.request();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void 등록된_사용자로_정상_로그인_시_토큰이_발급되어야_한다() {
        // given
        new Requester.RequesterBuilder(PATH + "/join", Method.POST)
                .body(join)
                .build()
                .request();
        AuthRequest.LoginReq login = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(password)
                .build();
        Requester requester = new Requester.RequesterBuilder(PATH + "/login", Method.POST)
                .spec(defaultSpec)
                .body(login)
                .description(loginDescription)
                .build();

        // when
        ExtractableResponse<Response> response = requester.request();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.jsonPath().getString("token")).isNotNull();
    }

    @Test
    void 존재하지_않는_회원이나_잘못된_패스워드로_로그인을_시도하면_인증이_실패되어야_한다() {
        // given
        new Requester.RequesterBuilder(PATH + "/join", Method.POST)
                .body(join)
                .build()
                .request();
        AuthRequest.LoginReq invalidPasswordLogin = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(password + "wrong")
                .build();
        AuthRequest.LoginReq nonExistentUserLogin = AuthRequest.LoginReq.builder()
                .loginId(loginId + "other")
                .password(password)
                .build();
        Requester invalidPasswordLoginRequester = new Requester.RequesterBuilder(PATH + "/login", Method.POST)
                .spec(failResponseSpec)
                .body(invalidPasswordLogin)
                .build();
        Requester nonExistentUserLoginRequester = new Requester.RequesterBuilder(PATH + "/login", Method.POST)
                .spec(failResponseSpec)
                .body(nonExistentUserLogin)
                .build();

        // when
        ExtractableResponse<Response> invalidPasswordResponse = invalidPasswordLoginRequester.request();
        ExtractableResponse<Response> nonexistentUserResponse = nonExistentUserLoginRequester.request();

        // then
        assertThat(invalidPasswordResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
        assertThat(nonexistentUserResponse.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }
}
