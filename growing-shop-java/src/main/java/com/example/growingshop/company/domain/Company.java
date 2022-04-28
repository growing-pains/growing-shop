package com.example.growingshop.company.domain;

import com.example.growingshop.validator.StringContain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Company {
    @EmbeddedId
    private CompanyId id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    @StringContain(checkLowerEn = true, checkUpperEn = true, checkNumber = true, hasSpecialCharacter = "-.()[]")
    private String name;

    @Column(nullable = false)
    private String businessRegistrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyGrade grade = CompanyGrade.BRONZE;
}
