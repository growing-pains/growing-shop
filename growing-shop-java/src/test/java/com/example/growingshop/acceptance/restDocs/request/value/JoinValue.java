package com.example.growingshop.acceptance.restDocs.request.value;

import com.example.growingshop.domain.auth.dto.AuthRequest;

public class JoinValue implements Value<AuthRequest.JoinReq> {
    private AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
            .name("신규유저")
            .mobile("01000000000")
            .email("growing-shop@growing.shop")
            .loginId("growing-shop")
            .password("1234")
            .build();

    @Override
    public AuthRequest.JoinReq getSuccessRequestValue() {
        return join;
    }

    @Override
    public AuthRequest.JoinReq getFailRequestValue() {
        return join;
    }
}
