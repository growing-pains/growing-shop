package com.example.domain.order

import com.example.domain.common.BaseEntity
import com.example.domain.product.Product
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

@Entity
class OrderLine(
    @ManyToOne
    @JoinColumn(nullable = false)
    val product: Product,

    @Column(nullable = false)
    @NotNull
    @Min(1)
    val price: Int,

    @Column(nullable = false)
    @NotNull
    @Min(1)
    val quantity: Int,

    isDeleted: Boolean
) : BaseEntity() {
    var isDeleted: Boolean = isDeleted
        protected set

    fun amounts(): Long {
        return price * quantity.toLong()
    }

    fun delete() {
        isDeleted = true
    }
}
