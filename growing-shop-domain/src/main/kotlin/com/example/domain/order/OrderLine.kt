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

    var isDeleted: Boolean
) : BaseEntity() {

    fun amounts(): Long {
        return price * quantity.toLong()
    }

//    public void update(OrderRequest.OrderLineReq req) {
//        this.productId = req.getProductId();
//        this.price = req.getPrice();
//        this.quantity = req.getQuantity();
//    }

    fun delete() {
        isDeleted = true
    }
}
