package com.example.growingshop.acceptance.restDocs.request.auth;

import com.example.growingshop.acceptance.restDocs.request.AcceptanceTestDocsRequest;
import com.example.growingshop.acceptance.restDocs.request.Requester;
import com.example.growingshop.acceptance.restDocs.value.auth.PolicyValue;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static com.example.growingshop.acceptance.AcceptanceTest.defaultSpec;
import static com.example.growingshop.acceptance.AcceptanceTest.failSpec;

public class PolicyRequester implements AcceptanceTestDocsRequest {
    private static final String PATH = "/policies";

    private final PolicyValue policyValue = PolicyValue.getInstance();
    private final Requester successDocsRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(defaultSpec)
            .body(policyValue.getSuccessRequestValue())
            .description(policyValue)
            .build();
    private final Requester failDocsRequester = new Requester.RequesterBuilder(PATH, Method.POST)
            .spec(failSpec)
            .body(policyValue.getFailRequestValue())
            .build();

    @Override
    public ExtractableResponse<Response> successRequestWithDocs() {
        return successDocsRequester.request();
    }

    @Override
    public ExtractableResponse<Response> failRequestWithDocs() {
        return failDocsRequester.request();
    }
}
