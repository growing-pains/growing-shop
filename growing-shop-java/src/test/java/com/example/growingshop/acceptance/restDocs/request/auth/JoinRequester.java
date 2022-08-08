package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.BodyDocsDescription;
import com.example.growingshop.acceptance.restDocs.Description;
import com.example.growingshop.acceptance.restDocs.Requester;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestRequest;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;

public class JoinRequester extends BodyDocsDescription implements AcceptanceTestDocsRequest, AcceptanceTestRequest {
    private static final String PATH = "/auth";
    private static final String NAME = "auth/join";
    private static final Description DESCRIPTION = new Description.DescriptionBuilder()
            .add("name", "이름")
            .add("mobile", "핸드폰 번호")
            .add("email", "이메일")
            .add("loginId", "로그인 아이디")
            .add("password", "패스워드")
            .addIgnore("company")
            .build();

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
