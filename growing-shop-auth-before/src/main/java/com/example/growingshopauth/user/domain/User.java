package com.example.growingshopauth.user.domain;

import com.example.growingshopauth.auth.domain.Roles;
import com.example.growingshopauth.company.domain.Company;
import com.example.growingshopcommon.validator.StringChecker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Pattern(regexp = "^[a-zA-Z가-힣'-.]+$")
    private String name;

    @Column(nullable = false)
    @Size(max = 13, min = 9)
    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-?\\d{3,4}-?\\d{4}$")
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
