package com.example.growingshop.domain.order.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO - 모든 엔티티에 auditing 적용
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime orderAt;

    @Column(name = "user")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "order", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @Builder
    public Order(LocalDateTime orderAt, List<OrderLine> orderLines) {
        this.orderAt = orderAt;
        this.orderLines = orderLines;
        this.status = OrderStatus.WAITING;
    }

    public Long totalPrice() {
        return orderLines.stream().mapToLong(OrderLine::amounts).sum();
    }

    public void delete() {
        this.status = OrderStatus.DELETED;
    }
}
