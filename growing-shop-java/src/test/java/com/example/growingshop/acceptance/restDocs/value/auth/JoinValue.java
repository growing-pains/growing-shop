package com.example.growingshop.acceptance.restDocs.value.auth;

import com.example.growingshop.acceptance.restDocs.description.BodyDocsDescription;
import com.example.growingshop.acceptance.restDocs.description.Description;
import com.example.growingshop.acceptance.restDocs.value.Value;
import com.example.growingshop.domain.auth.dto.AuthRequest;

public class JoinValue extends BodyDocsDescription implements Value<AuthRequest.JoinReq> {
    private static final JoinValue INSTANCE = new JoinValue();
    private static final String NAME = "auth/join";

    private JoinValue() {
        super(
                NAME,
                new Description.DescriptionBuilder()
                        .add("name", "이름")
                        .add("mobile", "핸드폰 번호")
                        .add("email", "이메일")
                        .add("loginId", "로그인 아이디")
                        .add("password", "패스워드")
                        .add("company", "업체 id", "Long")
                        .build()
        );
    }

    @Override
    public AuthRequest.JoinReq getSuccessRequestValue() {
        return AuthRequest.JoinReq.builder()
                .name("신규유저")
                .mobile("01000000000")
                .email("growing-shop@growing.shop")
                .loginId("growing-shop")
                .password("1234")
                .build();
    }

    @Override
    public AuthRequest.JoinReq getFailRequestValue() {
        return AuthRequest.JoinReq.builder()
                .name("신규유저")
                .mobile("01000000000")
                .email("growing-shop@growing.shop")
                .loginId("growing-shop" + "wrong")
                .password("1234" + "wrong")
                .build();
    }

    public static JoinValue getInstance() {
        return INSTANCE;
    }
}
