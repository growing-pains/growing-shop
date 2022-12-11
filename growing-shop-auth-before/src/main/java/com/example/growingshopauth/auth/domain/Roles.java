package com.example.growingshopauth.auth.domain;

import com.example.growingshopauth.auth.dto.RoleResponse;
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
public class Roles {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles = new ArrayList<>();


    public Roles(List<Role> roles) {
        this.roles.addAll(roles);
    }

    public Roles addRoles(Role other) {
        List<Role> result = new ArrayList<>(roles);
        result.add(other);

        return new Roles(result);
    }

    public List<Policy> getAllPolicy() {
        return roles.stream()
                .flatMap(role -> role.getPolicies().getPolicies().stream())
                .collect(Collectors.toList());
    }

    public List<RoleResponse.RoleRes> toResponse() {
        return this.roles.stream()
                .map(RoleResponse.RoleRes::from)
                .collect(Collectors.toList());
    }
}
