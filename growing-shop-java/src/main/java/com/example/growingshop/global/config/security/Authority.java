package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.HttpMethod;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.domain.Roles;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
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
