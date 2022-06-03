package com.example.growingshop.acceptance.helper;

import com.example.growingshop.domain.auth.dto.AuthRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;

public class AuthRequestHelper {
    private static final String PATH = "/auth";

    private AuthRequestHelper() {}

    public static ExtractableResponse<Response> joinRequest(AuthRequest.JoinReq req) {
        return RequestHelper.postRequest(PATH + "/join", new HashMap<>(), req);
    }

    public static ExtractableResponse<Response> loginRequest(AuthRequest.LoginReq req) {
        return RequestHelper.postRequest(PATH + "/login", new HashMap<>(), req);
    }
}
