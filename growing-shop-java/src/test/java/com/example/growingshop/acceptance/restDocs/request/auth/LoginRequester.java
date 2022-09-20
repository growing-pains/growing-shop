package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import com.example.growingshop.acceptance.restDocs.request.Requester;
import com.example.growingshop.acceptance.restDocs.value.auth.LoginValue;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;
import static com.example.growingshop.acceptance.AcceptanceTest.failSpec;

public class LoginRequester implements AcceptanceTestDocsRequest {
    private static final String PATH = "/auth/login";

    private final LoginValue loginValue = LoginValue.getInstance();
    private final Requester successRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(defaultSpec)
            .body(loginValue.getSuccessRequestValue())
            .description(loginValue)
            .build();
    private final Requester failRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(failSpec)
            .body(loginValue.getFailRequestValue())
            .build();

    @Override
    public ExtractableResponse<Response> successRequestWithDocs() {
        return successRequester.request();
    }

    @Override
    public ExtractableResponse<Response> failRequestWithDocs() {
        return failRequester.request();
    }
}
