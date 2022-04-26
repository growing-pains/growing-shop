package com.example.growingshop.company.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "[a-zA-Z\\d\\-\\.()\\[\\]]*")
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
