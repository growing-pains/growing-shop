package com.example.growingshopauth.domain;

import com.example.growingshopauth.dto.RoleResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Policies {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_policy",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "policy_id", referencedColumnName = "id")
    )
    private List<Policy> policies = new ArrayList<>();

    public Policies(List<Policy> policies) {
        this.policies.addAll(policies);
    }

    public boolean containPath(String path) {
        return allAccessiblePath().stream()
                .anyMatch(accessiblePath -> accessiblePath.equals(path));
    }

    public List<RoleResponse.PoliciesRes> toResponse() {
        return this.policies.stream()
                .map(RoleResponse.PoliciesRes::from)
                .collect(Collectors.toList());
    }

    private List<String> allAccessiblePath() {
        return policies.stream()
                .map(Policy::getPath)
                .collect(Collectors.toList());
    }
}
