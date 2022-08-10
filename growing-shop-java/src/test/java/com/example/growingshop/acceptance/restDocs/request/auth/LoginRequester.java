package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.description.BodyDocsDescription;
import com.example.growingshop.acceptance.restDocs.description.Description;
import com.example.growingshop.acceptance.restDocs.request.Requester;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import com.example.growingshop.acceptance.restDocs.value.auth.LoginValue;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;
import static com.example.growingshop.acceptance.AcceptanceTest.failResponseSpec;

public class LoginRequester extends BodyDocsDescription implements AcceptanceTestDocsRequest {
    private static final String PATH = "/auth/login";
    private static final String NAME = "auth/login";
    private static final Description DESCRIPTION = new Description.DescriptionBuilder()
            .add("loginId", "로그인 아이디")
            .add("password", "패스워드")
            .build();

    private final LoginValue loginValue = new LoginValue();
    private final Requester successRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(defaultSpec)
            .body(loginValue.getSuccessRequestValue())
            .description(this)
            .build();
    private final Requester failRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(failResponseSpec)
            .body(loginValue.getFailRequestValue())
            .build();

    public LoginRequester() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public ExtractableResponse<Response> successRequestWithDocs() {
        return successRequester.request();
    }

    @Override
    public ExtractableResponse<Response> failRequestWithDocs() {
        return failRequester.request();
    }
}
