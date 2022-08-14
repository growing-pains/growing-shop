package com.example.growingshop.domain.auth.domain;

import com.example.growingshop.domain.auth.dto.Authority;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Policies policies = new Policies();

    public Role(String name, List<Policy> policies) {
        this.name = name;
        this.policies = new Policies(policies);
    }

    public Authority getGrantedAuthorities() {
        return new Authority(name, policies.allAccessiblePath());
    }

    public void changePolicies(Policies target) {
        this.policies = target;
    }
}
