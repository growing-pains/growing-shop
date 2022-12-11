package com.example.growingshopauth.auth.domain;

import jakarta.persistence.*;
import lombok.*;

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

    public void changePolicies(Policies target) {
        this.policies = target;
    }
}
