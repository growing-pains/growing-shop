package com.example.growingshop.domain.order.repository;

import com.example.growingshop.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.status <> com.example.growingshop.domain.order.domain.OrderStatus.DELETED")
    List<Order> findAllNotDeleted();

    @Query("select o from Order o where o.userId = :userId and o.status <> com.example.growingshop.domain.order.domain.OrderStatus.DELETED")
    List<Order> findAllNotDeletedByUserId(Long userId);

    @Query("select o from Order o where o.id = :id and o.userId = :userId and o.status <> com.example.growingshop.domain.order.domain.OrderStatus.DELETED")
    Optional<Order> findNotDeletedByIdAndUserId(Long id, Long userId);
}
