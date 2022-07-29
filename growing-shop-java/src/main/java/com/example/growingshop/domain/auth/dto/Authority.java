package com.example.growingshop.domain.auth.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class Authority implements GrantedAuthority {
    private static final String rolePrefix = "ROLE_";

    private String role;
    private List<String> allowPaths = new ArrayList<>();

    public Authority(String role, List<String> allowPaths) {
        this.role = rolePrefix + role;
        this.allowPaths.addAll(allowPaths);
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public boolean isAllowAccessPath(String path) {
        return allowPaths.contains(path);
    }
}
