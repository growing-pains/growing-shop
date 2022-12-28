package com.example.growingshopauth.user.domain

import com.example.growingshopauth.auth.domain.Roles
import com.example.growingshopauth.company.domain.Company
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity(name = "`user`")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^[a-zA-Z가-힣'-.]+$")
    val name: String,

    @Column(nullable = false)
    @Size(max = 13, min = 9)
    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-?\\d{3,4}-?\\d{4}$")
    val mobile: String,

    @Column(nullable = false)
    @Email
    @NotBlank
    val email: String,

    @Size(max = 30)
    @Pattern(regexp = "^[a-zA-Z가-힣-_]+$")
    @Column(nullable = false)
    val loginId: String,

    @JsonIgnore
    @Column(nullable = false)
    var password: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY")
    val company: Company? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: UserStatus,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: UserType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val grade: UserGrade,

    @Embedded
    var roles: Roles = Roles()
)
