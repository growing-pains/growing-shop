package com.example.growingshop.acceptance.auth;

import com.example.growingshop.acceptance.helper.DefaultRequestHelper;
import com.example.growingshop.domain.auth.dto.AuthRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;

public class AuthRequestHelper {
    private static final String PATH = "/auth";

    private AuthRequestHelper() {}

    public static ExtractableResponse<Response> joinRequest(
            String name, String mobile, String email, String loginId, String password
    ) {
        return DefaultRequestHelper.postRequest(
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
            String loginId, String password
    ) {
        return DefaultRequestHelper.postRequest(
                PATH + "/login",
                new HashMap<>(),
                AuthRequest.LoginReq.builder()
                        .loginId(loginId)
                        .password(password)
                        .build()
        );
    }
}
