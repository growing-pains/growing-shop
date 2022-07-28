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
    private Privileges privileges = new Privileges();

    public Role(String name, List<Privilege> privileges) {
        this.name = name;
        this.privileges = new Privileges(privileges);
    }

    public Authority getGrantedAuthorities() {
        return new Authority(name, privileges.allAccessiblePath());
    }

    public void changePrivileges(Privileges target) {
        this.privileges = target;
    }
}
