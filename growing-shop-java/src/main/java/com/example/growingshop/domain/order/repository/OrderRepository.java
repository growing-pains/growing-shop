package com.example.growingshop.domain.order.repository;

import com.example.growingshop.domain.order.domain.Order;
import com.example.growingshop.domain.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
    Optional<Order> findByIdAndUserId(Long id, Long userId);

//    @Query("select o from `order` o where o.is_deleted != 'DELETED'")
//    List<Order> findAllNotDeleted();
}
