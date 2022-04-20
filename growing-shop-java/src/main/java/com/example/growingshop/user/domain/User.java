package com.example.growingshop.user.domain;

import com.example.growingshop.company.domain.CompanyId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String loginId;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "id", column = @Column(name = "company")))
    private CompanyId company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserGrade grade = UserGrade.NORMAL;
}
