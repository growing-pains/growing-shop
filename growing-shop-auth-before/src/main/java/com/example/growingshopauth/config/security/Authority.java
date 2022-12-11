package com.example.growingshopauth.config.security;

import com.example.growingshopauth.auth.domain.HttpMethod;
import com.example.growingshopauth.auth.domain.Role;
import com.example.growingshopauth.auth.domain.Roles;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        return rolePrefix + name;
    }

    private void addAccessible(String path, HttpMethod method) {
        accessible.put(path, ImmutableSet.copyOf(
                Iterables.concat(
                        accessible.getOrDefault(path, Collections.emptySet()),
                        method.getMethod()
                )
        ));
    }
}
