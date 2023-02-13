package com.example.growingshoporder.repository;

import com.example.domain.order.Order;
import com.example.repository.order.OrderRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpandOrderRepository extends OrderRepository {
    @Query("select o from Order o where o.status <> com.example.domain.order.OrderStatus.DELETED")
    List<Order> findAllNotDeleted();

    @Query("select o from Order o where o.user.id = :userId and o.status <> com.example.domain.order.OrderStatus.DELETED")
    List<Order> findAllNotDeletedByUserId(Long userId);

    @Query("select o from Order o where o.id = :id and o.user.id = :userId and o.status <> com.example.domain.order.OrderStatus.DELETED")
    Optional<Order> findNotDeletedByIdAndUserId(Long id, Long userId);
}
