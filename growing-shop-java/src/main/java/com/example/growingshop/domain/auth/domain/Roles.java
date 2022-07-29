package com.example.growingshop.domain.auth.domain;

import com.example.growingshop.domain.auth.dto.Authorities;
import com.example.growingshop.domain.auth.dto.RoleResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    public Authorities getGrantedAuthorities() {
        return new Authorities(
                roles.stream()
                        .map(Role::getGrantedAuthorities)
                        .collect(Collectors.toList())
        );
    }

    public Roles combineWithUserDefaultRole(Role userDefaultRole) {
        List<Role> result = new ArrayList<>(this.roles);
        result.add(userDefaultRole);

        return new Roles(result);
    }

    public List<RoleResponse.RoleRes> toResponse() {
        return this.roles.stream()
                .map(RoleResponse.RoleRes::from)
                .collect(Collectors.toList());
    }
}
