package com.example.growingshop.acceptance.restDocs.request.value;

import com.example.growingshop.domain.auth.dto.AuthRequest;

public class LoginValue implements Value<AuthRequest.LoginReq> {
    @Override
    public AuthRequest.LoginReq getSuccessRequestValue() {
        return AuthRequest.LoginReq.builder()
                .loginId("growing-shop")
                .password("1234")
                .build();
    }

    @Override
    public AuthRequest.LoginReq getFailRequestValue() {
        return AuthRequest.LoginReq.builder()
                .loginId("growing-shop" + "wrong")
                .password("1234" + "wrong")
                .build();
    }
}
