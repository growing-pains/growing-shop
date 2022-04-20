package com.example.growingshop.company.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Company {
    @EmbeddedId
    private CompanyId id;

    @Column(nullable = false)
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
