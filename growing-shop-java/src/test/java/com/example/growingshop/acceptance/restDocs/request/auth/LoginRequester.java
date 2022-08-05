package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.BodyDescription;
import com.example.growingshop.acceptance.restDocs.Requester;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;

public class LoginRequester extends BodyDescription implements AcceptanceTestDocsRequest {
    private static final String PATH = "/auth";
    private static final String NAME = "auth/login";
    private static final Map<String, String> DESCRIPTION = Map.of(
            "loginId", "로그인 아이디",
            "password", "패스워드"
    );

    private final LoginValue loginValue = new LoginValue();
    private final Requester successRequester = new Requester.RequesterBuilder(PATH + "/login", Method.POST)
            .spec(defaultSpec)
            .body(loginValue.getSuccessRequestValue())
            .description(this)
            .build();
    private final Requester failRequester = new Requester.RequesterBuilder(PATH + "/login", Method.POST)
            .spec(defaultSpec)
            .body(loginValue.getFailRequestValue())
            .description(this)
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
