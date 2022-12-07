package com.example.growingshoporder.domain;

import com.example.growingshoporder.dto.OrderRequest;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

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

    //    @ManyToOne(targetEntity = User.class)
//    @JoinColumn(name="user", nullable = false)
    // TODO - 단순 관계 표현을 위해 다른 모듈을 import 해야 할지, 아니면 엔티티 전체를 별도로 관리할지 등 고민 필요
    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Valid
    @NotEmpty
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "`order`")
    @Where(clause = "is_deleted = 0")
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

    public void upsertOrderLines(List<OrderRequest.OrderLineReq> req) {
        orderLines.forEach(orderLine ->
                req.stream()
                        .filter(orderLineReq -> orderLine.getId().equals(orderLineReq.getId())).findFirst()
                        .ifPresent(orderLine::update)
        );
        orderLines.stream()
                .filter(line -> req.stream().noneMatch(orderLineReq -> orderLineReq.getId().equals(line.getId())))
                .forEach(OrderLine::delete);
        req.stream()
                .filter(orderLineReq -> orderLineReq.getId() == null)
                .forEach(orderLineReq -> orderLines.add(orderLineReq.toEntity()));
    }
}