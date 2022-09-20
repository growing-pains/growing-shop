package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.AcceptanceTest;
import com.example.growingshop.acceptance.restDocs.request.auth.PolicyRequester;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("정책 관련 기능")
class PolicyAcceptanceTest extends AcceptanceTest {

    private PolicyRequester policyRequester;

    protected void setUp() {
        policyRequester = new PolicyRequester();
    }

    @Test
    void 정책_생성_시_정상_생성되어야_한다() {
        // when
        ExtractableResponse<Response> response = policyRequester.successRequestWithDocs();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    void 잘못된_path_로_정책_생성_시_실패해야_한다() {
        // when
        ExtractableResponse<Response> response = policyRequester.failRequestWithDocs();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }
}
