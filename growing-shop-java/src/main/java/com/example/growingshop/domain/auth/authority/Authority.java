package com.example.growingshop.domain.auth.authority;

import com.example.growingshop.domain.auth.domain.HttpMethod;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.domain.Roles;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public class Authority implements GrantedAuthority {
    private static final String rolePrefix = "ROLE_";

    private String name;
    private Roles roles;
    private Map<String, Set<HttpMethod>> accessible = new HashMap<>();

    public Authority(String name, Role userTypeRoles, Roles userRoles) {
        this.name = name;
        this.roles = userRoles.addRoles(userTypeRoles);

        roles.getAllPolicy()
                .forEach(policy -> addAccessible(policy.getPath(), policy.getMethod()));
    }

    public boolean possibleAccess(String path, HttpMethod httpMethod) {
        Set<HttpMethod> accessibleMethods = accessible.get(path);

        if (accessibleMethods == null) {
            return false;
        }

        return accessibleMethods.contains(httpMethod);
    }

    @Override
    public String getAuthority() {
        return name;
    }

    private void addAccessible(String path, HttpMethod method) {
        Set<HttpMethod> methods = accessible.getOrDefault(path, Collections.emptySet());
        methods.addAll(method.getMethod());
        accessible.put(path, methods);
    }
}
