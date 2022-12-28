package com.example.growingshopauth.auth.dto;

import com.example.growingshopauth.auth.domain.HttpMethod;
import com.example.growingshopauth.auth.domain.Policies;
import com.example.growingshopauth.auth.domain.Policy;
import com.example.growingshopauth.auth.domain.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class RoleRequest {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreateRole {
        private String name;
        private List<Long> policies = new ArrayList<>();

        public Role toEntity(Policies policies) {
            return Role.builder().name(name).policies(policies).build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ChangeRolePolicies {
        private Long role;
        private List<Long> policies = new ArrayList<>();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreatePolicy {
        private String name;
        private String path;
        private HttpMethod method;
        private String description;

        public Policy toEntity() {
            return Policy.builder()
                    .name(name)
                    .path(path)
                    .method(method)
                    .description(description)
                    .build();
        }
    }
}
