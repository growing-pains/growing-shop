package com.example.growingshop.domain.user.domain;

public enum UserType {
    NORMAL("ROLE_USER"),
    SELLER("ROLE_SELLER"),
    ADMIN("ROLE_ADMIN");

    private String defaultRole;

    UserType(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public String getDefaultRole() {
        return defaultRole;
    }
}
