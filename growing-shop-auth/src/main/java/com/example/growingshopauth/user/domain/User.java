package com.example.growingshop.domain.user.domain;

import com.example.growingshop.domain.auth.domain.Roles;
import com.example.growingshop.domain.company.domain.Company;
import com.example.growingshop.global.validator.StringChecker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    @StringChecker(includeLowerEn = true, includeUpperEn = true, includeKo = true, includeSpecialCharacter = "'-.")
    private String name;

    @Column(nullable = false)
    @Size(max = 13, min = 9)
    @NotBlank
    @Pattern(regexp = "^[0-9]{2,3}-?[0-9]{3,4}-?[0-9]{4}$")
    private String mobile;

    @Column(nullable = false)
    @Email
    @NotBlank
    private String email;

    @Size(max = 30)
    @StringChecker(includeLowerEn = true, includeUpperEn = true, includeNumber = true, includeSpecialCharacter = "-_")
    @Column(nullable = false)
    private String loginId;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserGrade grade;

    @Embedded
    private Roles roles;
}
