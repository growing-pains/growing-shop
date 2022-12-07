package com.example.growingshoporder.repository;

import com.example.growingshoporder.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.status <> com.example.growingshoporder.domain.OrderStatus.DELETED")
    List<Order> findAllNotDeleted();

    @Query("select o from Order o where o.userId = :userId and o.status <> com.example.growingshoporder.domain.OrderStatus.DELETED")
    List<Order> findAllNotDeletedByUserId(Long userId);

    @Query("select o from Order o where o.id = :id and o.userId = :userId and o.status <> com.example.growingshoporder.domain.OrderStatus.DELETED")
    Optional<Order> findNotDeletedByIdAndUserId(Long id, Long userId);
}
