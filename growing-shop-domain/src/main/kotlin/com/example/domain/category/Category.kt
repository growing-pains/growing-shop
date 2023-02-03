package com.example.domain.category

import com.example.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
class Category(
    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z가-힣-.\"']+$")
    val name: String,

    status: CategoryStatus
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var status: CategoryStatus = status
        protected set

    fun delete() {
        status = CategoryStatus.DELETED
    }

    fun inspect(status: CategoryStatus) {
        when (status) {
            CategoryStatus.REJECTED -> this.status = CategoryStatus.REJECTED
            CategoryStatus.NORMAL -> this.status = CategoryStatus.NORMAL
            else -> throw IllegalArgumentException("잘못된 검수 요청입니다.")
        }
    }
}
