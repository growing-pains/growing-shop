package com.example.domain.product

import com.example.domain.common.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
class Product(


    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z가-힣!@#$%^&*\"'-,.<>?:\\[\\]()]+$")
    var name: String,

    @Column(nullable = false)
    @Min(0)
    var price: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ProductStatus,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.MERGE, CascadeType.PERSIST], orphanRemoval = true)
    val categories: List<ProductCategory> = listOf()
) : BaseEntity()
