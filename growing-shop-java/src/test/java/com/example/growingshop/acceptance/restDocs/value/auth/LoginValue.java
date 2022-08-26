package com.example.growingshop.acceptance.restDocs.value.auth;

import com.example.growingshop.acceptance.restDocs.description.BodyDocsDescription;
import com.example.growingshop.acceptance.restDocs.description.Description;
import com.example.growingshop.acceptance.restDocs.value.Value;
import com.example.growingshop.domain.auth.dto.AuthRequest;

public class LoginValue extends BodyDocsDescription implements Value<AuthRequest.LoginReq> {
    private static final LoginValue INSTANCE = new LoginValue();
    private static final String NAME = "auth/login";

    private LoginValue() {
        super(
                NAME,
                new Description.DescriptionBuilder()
                        .add("loginId", "로그인 아이디")
                        .add("password", "패스워드")
                        .build()
        );
    }

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

    public static LoginValue getInstance() {
        return INSTANCE;
    }
}
