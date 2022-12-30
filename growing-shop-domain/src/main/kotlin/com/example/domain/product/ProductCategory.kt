package com.example.domain.product

import com.example.domain.category.Category
import com.example.domain.common.IdentifiableEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne

@Entity
class ProductCategory(
    @ManyToOne(fetch = FetchType.LAZY)
    val product: Product,

    @ManyToOne
    val category: Category
) : IdentifiableEntity()
