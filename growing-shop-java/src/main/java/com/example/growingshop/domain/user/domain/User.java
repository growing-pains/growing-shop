package com.example.growingshop.domain.user.domain;

import com.example.growingshop.domain.company.domain.CompanyId;
import com.example.growingshop.global.validator.StringAnyContain;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    @StringAnyContain(checkContainLowerEn = true, checkContainUpperEn = true, checkContainKo = true, hasContainSpecialCharacter = "'-.")
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
    @StringAnyContain(checkContainLowerEn = true, checkContainUpperEn = true, checkContainNumber = true, hasContainSpecialCharacter = "-_")
    @Column(nullable = false)
    private String loginId;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "company")))
    private CompanyId company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserGrade grade;

    public boolean isPersist() {
        return this.id > 0;
    }
}
