package com.example.growingshop.domain.auth.dto;

import com.example.growingshop.domain.auth.domain.Privilege;
import com.example.growingshop.domain.auth.domain.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class RoleResponse {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class RoleRes {
        private String name;
        private List<PrivilegeRes> privileges = new ArrayList<>();

        public static RoleRes from(Role role) {
            RoleRes res = new RoleRes();

            res.name = role.getName();
            res.privileges.addAll(
                    role.getPrivileges().toResponse()
            );

            return res;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class PrivilegeRes {
        private String name;
        private String path;
        private String description;

        public static PrivilegeRes from(Privilege privilege) {
            PrivilegeRes res = new PrivilegeRes();

            res.name = privilege.getName();
            res.path = privilege.getPath();
            res.description = privilege.getDescription();

            return res;
        }
    }
}
