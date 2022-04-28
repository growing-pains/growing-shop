package com.example.growingshop.user.domain;

import com.example.growingshop.company.domain.CompanyId;
import com.example.growingshop.validator.StringContain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @EmbeddedId
    private UserId id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    @StringContain(checkLowerEn = true, checkUpperEn = true, checkKo = true, hasSpecialCharacter = "'-.")
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
    @StringContain(checkLowerEn = true, checkUpperEn = true, checkNumber = true, hasSpecialCharacter = "-_")
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
    private UserGrade grade = UserGrade.NORMAL;
}
