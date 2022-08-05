package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.BodyDescription;
import com.example.growingshop.acceptance.restDocs.Requester;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestRequest;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;

public class JoinRequester extends BodyDescription implements AcceptanceTestDocsRequest, AcceptanceTestRequest {
    private static final String PATH = "/auth";
    private static final String NAME = "auth/join";
    private static final Map<String, String> DESCRIPTION = Map.of(
            "name", "이름",
            "mobile", "핸드폰 번호",
            "email", "이메일",
            "loginId", "로그인 아이디",
            "password", "패스워드",


            "company", "회사 정보",
            "joinPassword", "회원가입 비밀번호"
    );

    private final JoinValue joinValue = new JoinValue();
    private final Requester requester = new Requester.RequesterBuilder(PATH + "/join", Method.POST)
            .spec(defaultSpec)
            .body(joinValue.getSuccessRequestValue())
            .description(this)
            .build();
    private final Requester defaultRequest = new Requester.RequesterBuilder(PATH + "/join", Method.POST)
            .spec(defaultSpec)
            .body(joinValue.getSuccessRequestValue())
            .build();

    public JoinRequester() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public ExtractableResponse<Response> successRequestWithDocs() {
        return requester.request();
    }

    @Override
    public ExtractableResponse<Response> failRequestWithDocs() {
        requester.request();
        return requester.request();
    }

    @Override
    public ExtractableResponse<Response> successRequest() {
        return defaultRequest.request();
    }

    @Override
    public ExtractableResponse<Response> failRequest() {
        return null;
    }
}
