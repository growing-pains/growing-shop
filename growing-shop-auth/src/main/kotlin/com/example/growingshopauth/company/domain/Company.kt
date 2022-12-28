package com.example.growingshopauth.company.domain

import com.example.growingshopcommon.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
class Company(
    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^[a-zA-Z가-힣-.()\\[\\]]+$")
    val name: String,

    @Column(nullable = false)
    val businessRegistrationNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: CompanyStatus,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val grade: CompanyGrade
) : BaseEntity()
