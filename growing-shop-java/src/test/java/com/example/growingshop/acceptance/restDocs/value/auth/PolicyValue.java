package com.example.growingshop.acceptance.restDocs.value.auth;

import com.example.growingshop.acceptance.restDocs.description.BodyDocsDescription;
import com.example.growingshop.acceptance.restDocs.description.Description;
import com.example.growingshop.acceptance.restDocs.value.Value;
import com.example.growingshop.domain.auth.domain.HttpMethod;
import com.example.growingshop.domain.auth.dto.RoleRequest;

public class PolicyValue extends BodyDocsDescription implements Value<RoleRequest.CreatePolicy> {
    private static final PolicyValue INSTANCE = new PolicyValue();
    private static final String NAME = "policies";

    private PolicyValue() {
        super(
                NAME,
                new Description.DescriptionBuilder()
                        .add("name", "정책 이름")
                        .add("path", "허용 경로\n\n'/' 로 시작되는 정상적인 http path 만 허용됨")
                        .add("method", "허용 http method\n\nALL, GET, POST, PUT, PATCH, DELETE")
                        .add("description", "설명")
                        .build()
        );
    }

    @Override
    public RoleRequest.CreatePolicy getSuccessRequestValue() {
        return RoleRequest.CreatePolicy.builder()
                .name("정책 이름")
                .path("/path")
                .method(HttpMethod.ALL)
                .description("정책 설명")
                .build();
    }

    @Override
    public RoleRequest.CreatePolicy getFailRequestValue() {
        return RoleRequest.CreatePolicy.builder()
                .name("잘못된 정책 정책 경로")
                .path("path!")
                .method(HttpMethod.ALL)
                .build();
    }

    public static PolicyValue getInstance() {
        return INSTANCE;
    }
}
