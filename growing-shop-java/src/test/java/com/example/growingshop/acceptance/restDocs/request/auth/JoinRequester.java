package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.description.BodyDocsDescription;
import com.example.growingshop.acceptance.restDocs.description.Description;
import com.example.growingshop.acceptance.restDocs.request.Requester;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestRequest;
import com.example.growingshop.acceptance.restDocs.request.value.JoinValue;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;
import static com.example.growingshop.acceptance.AcceptanceTest.failResponseSpec;

public class JoinRequester extends BodyDocsDescription implements AcceptanceTestDocsRequest, AcceptanceTestRequest {
    private static final String PATH = "/auth/join";
    private static final String NAME = "auth/join";
    private static final Description DESCRIPTION = new Description.DescriptionBuilder()
            .add("name", "이름")
            .add("mobile", "핸드폰 번호")
            .add("email", "이메일")
            .add("loginId", "로그인 아이디")
            .add("password", "패스워드")
            .add("company", "업체 id", "Long")
            .build();

    private final JoinValue joinValue = new JoinValue();
    private final Requester successDocsRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(defaultSpec)
            .body(joinValue.getSuccessRequestValue())
            .description(this)
            .build();
    private final Requester failDocsRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(failResponseSpec)
            .body(joinValue.getFailRequestValue())
            .build();
    private final Requester defaultRequest = new Requester.RequesterBuilder(PATH, Method.POST)
            .body(joinValue.getSuccessRequestValue())
            .build();

    public JoinRequester() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public ExtractableResponse<Response> successRequestWithDocs() {
        return successDocsRequester.request();
    }

    @Override
    public ExtractableResponse<Response> failRequestWithDocs() {
        defaultRequest.request();
        return failDocsRequester.request();
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
