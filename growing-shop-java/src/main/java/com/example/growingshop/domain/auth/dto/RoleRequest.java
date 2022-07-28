package com.example.growingshop.domain.auth.dto;

import com.example.growingshop.domain.auth.domain.Privilege;
import com.example.growingshop.domain.auth.domain.Privileges;
import com.example.growingshop.domain.auth.domain.Role;
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

        private List<Long> privileges = new ArrayList<>();

        public Role toEntity(Privileges privileges) {
            return Role.builder().name(name).privileges(privileges).build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreatePrivilege {
        private String name;
        private String path;
        private String description;

        public Privilege toEntity() {
            return Privilege.builder()
                    .name(name)
                    .path(path)
                    .description(description)
                    .build();
        }
    }
}
