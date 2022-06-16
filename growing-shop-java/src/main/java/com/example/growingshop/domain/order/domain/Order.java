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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime orderAt;

    @Column(name = "user")
    private Long userId;

    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "order", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    public Long totalAmounts() {
        return orderLines.stream().mapToLong(OrderLine::amounts).sum();
    }

    @Builder
    public Order(LocalDateTime orderAt, Long userId, List<OrderLine> orderLines) {
        this.orderAt = orderAt;
        this.userId = userId;
        this.orderLines = orderLines;
    }
}
