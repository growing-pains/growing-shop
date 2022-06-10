package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.helper.RestDocsRequestHelper;
import com.example.growingshop.domain.auth.dto.AuthRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;

public class AuthRestDocsRequestHelper {
    private static final String PATH = "/auth";

    private AuthRestDocsRequestHelper() {}

    public static ExtractableResponse<Response> joinRequest(
            RequestSpecification spec, String name, String mobile, String email, String loginId, String password
    ) {
        return RestDocsRequestHelper.postRequest(
                spec,
                PATH + "/join",
                new HashMap<>(),
                AuthRequest.JoinReq.builder()
                        .name(name)
                        .mobile(mobile)
                        .email(email)
                        .loginId(loginId)
                        .password(password)
                        .build()
        );
    }

    public static ExtractableResponse<Response> loginRequest(
            RequestSpecification spec, String loginId, String password
    ) {
        return RestDocsRequestHelper.postRequest(
                spec,
                PATH + "/login",
                new HashMap<>(),
                AuthRequest.LoginReq.builder()
                        .loginId(loginId)
                        .password(password)
                        .build()
        );
    }
}
