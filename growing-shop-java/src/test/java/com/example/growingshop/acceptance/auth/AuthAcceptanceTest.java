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
}
