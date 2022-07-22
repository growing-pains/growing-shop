package com.example.growingshop.domain.auth.domain;

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
public class Privileges {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    )
    private List<Privilege> privileges = new ArrayList<>();

    public Privileges(List<Privilege> privileges) {
        this.privileges.addAll(privileges);
    }

    public List<String> getAllAccessiblePath() {
        return privileges.stream()
                .map(Privilege::getPath)
                .collect(Collectors.toList());
    }

    public boolean containPath(String path) {
        return getAllAccessiblePath().stream()
                .anyMatch(accessiblePath -> accessiblePath.equals(path));
    }
}
