package com.example.domain.order

import com.example.domain.common.BaseEntity
import com.example.domain.user.User
import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.hibernate.annotations.Where

@Entity
@Table(name = "`order`")
class Order(
    @ManyToOne
    @JoinColumn(nullable = false)
    val user: User,

    status: OrderStatus,

    @Valid
    @NotEmpty
    @OneToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], orphanRemoval = true)
    @JoinColumn(name = "`order`")
    @Where(clause = "is_deleted = 0")
    val orderLines: List<OrderLine> = listOf()
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = status
        protected set

    fun totalPrice(): Long {
        return orderLines.sumOf { it.amounts() }
    }

    fun delete() {
        status = OrderStatus.DELETED
    }
}
