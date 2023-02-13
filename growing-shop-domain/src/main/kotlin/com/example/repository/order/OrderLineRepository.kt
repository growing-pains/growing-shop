package com.example.repository.order

import com.example.domain.order.OrderLine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderLineRepository : JpaRepository<OrderLine, Long>
