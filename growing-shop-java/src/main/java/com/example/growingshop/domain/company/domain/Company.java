package com.example.growingshop.domain.company.domain;

import com.example.growingshop.global.validator.StringChecker;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    @StringChecker(includeLowerEn = true, includeUpperEn = true, includeNumber = true, includeSpecialCharacter = "-.()[]")
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
