package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestRequest;
import com.example.growingshop.acceptance.restDocs.request.Requester;
import com.example.growingshop.acceptance.restDocs.value.auth.JoinValue;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;
import static com.example.growingshop.acceptance.AcceptanceTest.failSpec;

public class JoinRequester implements AcceptanceTestDocsRequest, AcceptanceTestRequest {
    private static final String PATH = "/auth/join";

    private final JoinValue joinValue = JoinValue.getInstance();
    private final Requester successDocsRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(defaultSpec)
            .body(joinValue.getSuccessRequestValue())
            .description(joinValue)
            .build();
    private final Requester failDocsRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(failSpec)
            .body(joinValue.getFailRequestValue())
            .build();
    private final Requester defaultRequest = new Requester.RequesterBuilder(PATH, Method.POST)
            .body(joinValue.getSuccessRequestValue())
            .build();

    @Override
    public ExtractableResponse<Response> successRequestWithDocs() {
        return successDocsRequester.request();
    }

    @Override
    public ExtractableResponse<Response> failRequestWithDocs() {
        failDocsRequester.request();
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
