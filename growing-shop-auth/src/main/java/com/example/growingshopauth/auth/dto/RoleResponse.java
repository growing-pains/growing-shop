package com.example.growingshopauth.auth.dto;

import com.example.growingshopauth.auth.domain.Policy;
import com.example.growingshopauth.auth.domain.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class RoleResponse {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class RoleRes {
        private Long id;
        private String name;
        private List<PoliciesRes> policies = new ArrayList<>();

        public static RoleRes from(Role role) {
            RoleRes res = new RoleRes();

            res.id = role.getId();
            res.name = role.getName();
            res.policies.addAll(
                    role.getPolicies().toResponse()
            );

            return res;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class PoliciesRes {
        private Long id;
        private String name;
        private String path;
        private String description;

        public static PoliciesRes from(Policy policy) {
            PoliciesRes res = new PoliciesRes();

            res.id = policy.getId();
            res.name = policy.getName();
            res.path = policy.getPath();
            res.description = policy.getDescription();

            return res;
        }
    }
}
